$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.Drawing
$root=Split-Path $PSScriptRoot -Parent
$assets=Join-Path $root 'src/main/resources/assets/proyecto_intento'
# Packaging only: resize ImageGen sources, preserving pixels and transparency.
function Pack-Texture($name,$destination,$size,$icon) {
    $source=[Drawing.Bitmap]::FromFile((Join-Path $root "art/source/$name.png"))
    $left=0; $top=0; $w=$source.Width; $h=$source.Height
    if($icon) {
        $left=$w; $top=$h; $right=0; $bottom=0
        for($y=0;$y -lt $h;$y++) { for($x=0;$x -lt $w;$x++) {
            if($source.GetPixel($x,$y).A -gt 127) { $left=[Math]::Min($left,$x);$top=[Math]::Min($top,$y);$right=[Math]::Max($right,$x);$bottom=[Math]::Max($bottom,$y) }
        }}
        $w=$right-$left+1; $h=$bottom-$top+1
    }
    $limit=if($icon){$size-2}else{$size}
    $ratio=$limit/[Math]::Max($w,$h)
    $tw=[Math]::Max(1,[int][Math]::Round($w*$ratio));$th=[Math]::Max(1,[int][Math]::Round($h*$ratio))
    $target=New-Object Drawing.Bitmap($size,$size)
    $ox=[int][Math]::Floor(($size-$tw)/2);$oy=[int][Math]::Floor(($size-$th)/2)
    for($y=0;$y -lt $th;$y++) { for($x=0;$x -lt $tw;$x++) {
        $target.SetPixel($x+$ox,$y+$oy,$source.GetPixel($left+[int][Math]::Floor(($x+.5)*$w/$tw),$top+[int][Math]::Floor(($y+.5)*$h/$th)))
    }}
    $target.Save((Join-Path $assets $destination),[Drawing.Imaging.ImageFormat]::Png)
    $source.Dispose();$target.Dispose()
}
Pack-Texture 'plunger_imagegen' 'textures/item/desatascador.png' 16 $true
Pack-Texture 'fertilizer_imagegen' 'textures/item/estiercol.png' 16 $true
Pack-Texture 'ceramic_imagegen' 'textures/block/toilet_ceramic.png' 16 $false
Pack-Texture 'extractor_imagegen' 'textures/block/extractor_estiercol.png' 16 $false
Pack-Texture 'trader_imagegen' 'textures/entity/dung_beetle_trader.png' 64 $false
python (Join-Path $PSScriptRoot 'build-roadmap-models.py')
if($LASTEXITCODE -ne 0) { throw 'Roadmap model generation failed' }
