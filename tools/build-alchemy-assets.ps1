$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
$projectRoot = Split-Path $PSScriptRoot -Parent
$resources = Join-Path $projectRoot 'src/main/resources'
$assets = Join-Path $resources 'assets/proyecto_intento'
function Write-AlchemyJson($relative, $value) {
    [IO.File]::WriteAllText((Join-Path $resources $relative), ($value | ConvertTo-Json -Depth 25), [Text.UTF8Encoding]::new($false))
}
# Packaging only: fit the ImageGen original to the inventory grid, preserving alpha.
$source = [Drawing.Bitmap]::FromFile((Join-Path $projectRoot 'art/source/popo_v2.png'))
$left=$source.Width; $top=$source.Height; $right=0; $bottom=0
for ($y=0; $y -lt $source.Height; $y++) { for ($x=0; $x -lt $source.Width; $x++) {
    if ($source.GetPixel($x,$y).A -gt 127) {
        $left=[Math]::Min($left,$x); $right=[Math]::Max($right,$x)
        $top=[Math]::Min($top,$y); $bottom=[Math]::Max($bottom,$y)
    }
}}
if ($right -le $left) { throw 'Generated sprite has no visible alpha content.' }
$cropWidth=$right-$left+1; $cropHeight=$bottom-$top+1
$ratio=14.0/[Math]::Max($cropWidth,$cropHeight)
$targetWidth=[Math]::Max(1,[int][Math]::Round($cropWidth*$ratio)); $targetHeight=[Math]::Max(1,[int][Math]::Round($cropHeight*$ratio))
$sprite=New-Object Drawing.Bitmap(16,16)
$offsetX=[int][Math]::Floor((16-$targetWidth)/2); $offsetY=[int][Math]::Floor((16-$targetHeight)/2)
for ($y=0; $y -lt $targetHeight; $y++) { for ($x=0; $x -lt $targetWidth; $x++) {
    $sampleX=$left+[int][Math]::Floor(($x+0.5)*$cropWidth/$targetWidth)
    $sampleY=$top+[int][Math]::Floor(($y+0.5)*$cropHeight/$targetHeight)
    $sprite.SetPixel($x+$offsetX,$y+$offsetY,$source.GetPixel($sampleX,$sampleY))
}}
$sprite.Save((Join-Path $assets 'textures/item/popo.png'),[Drawing.Imaging.ImageFormat]::Png)
$sprite.Save((Join-Path $assets 'textures/mob_effect/stinky.png'),[Drawing.Imaging.ImageFormat]::Png)
$source.Dispose(); $sprite.Dispose()
foreach ($language in @('es_es','es_cl','en_us')) {
    $current=Get-Content -Raw -Encoding UTF8 (Join-Path $assets "lang/$language.json") | ConvertFrom-Json
    $strings=@{}; $current.PSObject.Properties | ForEach-Object { $strings[$_.Name]=$_.Value }
    $spanish=$language -ne 'en_us'
    $strings['item.minecraft.potion.effect.bottled_fart']=if($spanish){'Pedo en botella'}else{'Bottled Fart'}
    $strings['item.minecraft.splash_potion.effect.bottled_fart']=if($spanish){'Pedo en botella arrojadizo'}else{'Splash Bottled Fart'}
    $strings['item.minecraft.lingering_potion.effect.bottled_fart']=if($spanish){'Pedo en botella persistente'}else{'Lingering Bottled Fart'}
    $strings['item.minecraft.tipped_arrow.effect.bottled_fart']=if($spanish){'Flecha apestosa'}else{'Stinky Arrow'}
    $strings['subtitles.proyecto_intento.fart']=if($spanish){'Animal se tira un pedo'}else{'Animal farts'}
    Write-AlchemyJson "assets/proyecto_intento/lang/$language.json" $strings
}
$current=Get-Content -Raw -Encoding UTF8 (Join-Path $assets 'sounds.json') | ConvertFrom-Json
$sounds=@{}; $current.PSObject.Properties | ForEach-Object { $sounds[$_.Name]=$_.Value }
$sounds['fart']=@{subtitle='subtitles.proyecto_intento.fart';sounds=@('proyecto_intento:fart_1','proyecto_intento:fart_2','proyecto_intento:fart_3')}
Write-AlchemyJson 'assets/proyecto_intento/sounds.json' $sounds
Write-Output 'Remade poop sprite, effect icon, potion translations and fart sound event generated.'
