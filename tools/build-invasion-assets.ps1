$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.Drawing
$root=Split-Path $PSScriptRoot -Parent
$assets=Join-Path $root 'src/main/resources/assets/proyecto_intento'
# Packaging only: preserve generated artwork, resample to game resolution.
function Pack($name,$destination,$size,$icon) {
    $source=[Drawing.Bitmap]::FromFile((Join-Path $root "art/source/invasion/${name}.png"))
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
    $dest=Join-Path $assets $destination
    [IO.Directory]::CreateDirectory((Split-Path $dest -Parent)) | Out-Null
    $target.Save($dest,[Drawing.Imaging.ImageFormat]::Png)
    $source.Dispose();$target.Dispose()
}
foreach($name in @('beetle_soldier','beetle_artillery','slime_runner','slime_artillery','slime_colossus')) {
    $sourceName=if($name.StartsWith('beetle_')){"${name}_opaque"}else{$name}
    Pack $sourceName "textures/entity/invasion/$name.png" 64 $false
}
foreach($name in @('dung_shot','acid_spit')) {
    Pack $name "textures/item/invasion/$name.png" 16 $true
}
