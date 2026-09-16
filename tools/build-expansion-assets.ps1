# Run after build-assets.ps1. Reuses its helpers, then adds the expansion's resources.
$ErrorActionPreference = 'Stop'
. (Join-Path $PSScriptRoot 'build-base-assets.ps1')

# Normalize sampled inventory sprites to their visible bounds, with a common optical center.
function Center-Icon($tile) {
    $minX=16; $minY=16; $maxX=-1; $maxY=-1
    for($y=0;$y -lt 16;$y++) { for($x=0;$x -lt 16;$x++) {
        if($tile.GetPixel($x,$y).A -gt 127) { $minX=[Math]::Min($minX,$x); $maxX=[Math]::Max($maxX,$x); $minY=[Math]::Min($minY,$y); $maxY=[Math]::Max($maxY,$y) }
    }}
    if($maxX -lt 0) { throw 'Generated icon has no visible pixels' }
    $result=New-Object Drawing.Bitmap(16,16)
    $ox=[int][Math]::Floor((16-($maxX-$minX+1))/2); $oy=[int][Math]::Floor((16-($maxY-$minY+1))/2)
    for($y=$minY;$y -le $maxY;$y++) { for($x=$minX;$x -le $maxX;$x++) { $result.SetPixel($x-$minX+$ox,$y-$minY+$oy,$tile.GetPixel($x,$y)) }}
    return ,$result
}
$icons=[Drawing.Bitmap]::FromFile((Join-Path $projectDir 'art/source/armor_icons_v2.png'))
# Boots keep their original design; all four icons share the same centered canvas.
for($i=0;$i -lt 4;$i++) {
    $raw=Sample-Tile $icons (($i % 2)*$icons.Width/2) ([Math]::Floor($i/2)*$icons.Height/2) ($icons.Width/2) ($icons.Height/2) 16
    if($i -eq 3) { $raw.Dispose(); $raw=New-Object Drawing.Bitmap((Join-Path $assetDir 'textures/item/popo_boots.png')) }
    if($i -eq 0 -and (Test-Path (Join-Path $projectDir 'art/source/helmet_v2.png'))) {
        $raw.Dispose(); $helmet=[Drawing.Bitmap]::FromFile((Join-Path $projectDir 'art/source/helmet_v2.png'))
        # Trim the generator's excess canvas and preserve the source pixel aspect ratio.
        $minHX=$helmet.Width; $minHY=$helmet.Height; $maxHX=-1; $maxHY=-1
        for($hy=0;$hy -lt $helmet.Height;$hy++) { for($hx=0;$hx -lt $helmet.Width;$hx++) {
            if($helmet.GetPixel($hx,$hy).A -gt 127) {$minHX=[Math]::Min($minHX,$hx);$maxHX=[Math]::Max($maxHX,$hx);$minHY=[Math]::Min($minHY,$hy);$maxHY=[Math]::Max($maxHY,$hy)}
        }}
        $sw=$maxHX-$minHX+1; $sh=$maxHY-$minHY+1
        $dw=12; $dh=[int][Math]::Round(12*$sh/$sw)
        $raw=New-Object Drawing.Bitmap(16,16)
        for($hy=0;$hy -lt $dh;$hy++) { for($hx=0;$hx -lt $dw;$hx++) {
            $pixel=$helmet.GetPixel($minHX+[int][Math]::Floor(($hx+0.5)*$sw/$dw),$minHY+[int][Math]::Floor(($hy+0.5)*$sh/$dh))
            $raw.SetPixel($hx+2,$hy+[int][Math]::Floor((16-$dh)/2),$pixel)
        }}
        $helmet.Dispose()
    }
    $centered=Center-Icon $raw; $raw.Dispose(); Save-Png $centered "textures/item/$($names[$i]).png"; $centered.Dispose()
}
$icons.Dispose()
$chitin=[Drawing.Bitmap]::FromFile((Join-Path $projectDir 'art/source/beetle_chitin.png'))
$material=Sample-Tile $chitin 0 0 $chitin.Width $chitin.Height 16; $chitin.Dispose()
$wet=[Drawing.Bitmap]::FromFile((Join-Path $assetDir 'textures/block/wet_popo.png'))
$skin=New-Object Drawing.Bitmap(64,64)
for($y=0;$y -lt 64;$y++) { for($x=0;$x -lt 64;$x++) {
    $sample=if($x -ge 32 -and $y -ge 32) {$wet} else {$material}
    $skin.SetPixel($x,$y,$sample.GetPixel($x % 16,$y % 16))
}}
Save-Png $skin 'textures/entity/dung_beetle.png'; $skin.Dispose(); $wet.Dispose(); $material.Dispose()
$fly=[Drawing.Bitmap]::FromFile((Join-Path $projectDir 'art/source/fly.png'))
$sprite=Sample-Tile $fly 0 0 $fly.Width $fly.Height 8
Save-Png $sprite 'textures/particle/fly.png'; $sprite.Dispose(); $fly.Dispose()
Write-Json 'assets/proyecto_intento/particles/fly.json' @{textures=@('proyecto_intento:fly')}
# The status effect uses the existing poop icon so it never has a missing HUD sprite.
Copy-Item (Join-Path $assetDir 'textures/item/popo.png') (New-Item -ItemType Directory -Force (Join-Path $assetDir 'textures/mob_effect')).FullName
Move-Item -LiteralPath (Join-Path $assetDir 'textures/mob_effect/popo.png') -Destination (Join-Path $assetDir 'textures/mob_effect/stinky.png') -Force
Write-Json 'assets/proyecto_intento/models/item/dung_beetle_spawn_egg.json' @{parent='minecraft:item/template_spawn_egg'}
$variants=@{}
for($amount=1;$amount -le 8;$amount++) {
    $variants["amount=$amount"]=@{model="proyecto_intento:block/popo_pile_$amount"}
    $faces=@{}; foreach($face in @('up','down','north','south','east','west')) {$faces[$face]=@{texture='#all'}}
    Write-Json "assets/proyecto_intento/models/block/popo_pile_$amount.json" @{parent='minecraft:block/block';textures=@{all='proyecto_intento:block/wet_popo';particle='proyecto_intento:block/wet_popo'};elements=@(@{from=@(2,0,2);to=@(14,(2+$amount),14);faces=$faces})}
}
Write-Json 'assets/proyecto_intento/blockstates/popo_pile.json' @{variants=$variants}
Write-Json 'assets/proyecto_intento/models/item/popo_pile.json' @{parent='proyecto_intento:block/popo_pile_1'}
Write-Json 'data/proyecto_intento/recipe/popo_pile.json' @{type='minecraft:crafting_shapeless';category='building';ingredients=@(@{item='proyecto_intento:popo'});result=@{id='proyecto_intento:popo_pile';count=1}}
Write-Json 'data/proyecto_intento/advancement/recipes/popo_pile.json' @{parent='minecraft:recipes/root';criteria=@{has_ingredient=@{trigger='minecraft:inventory_changed';conditions=@{items=@(@{items=@('proyecto_intento:popo')})}}};requirements=@(,@('has_ingredient'));rewards=@{recipes=@('proyecto_intento:popo_pile')}}
Write-Json 'data/minecraft/tags/block/mineable/shovel.json' @{replace=$false;values=@('proyecto_intento:wet_popo','proyecto_intento:popo_pile')}
Write-Json 'assets/proyecto_intento/sounds.json' @{
    flies=@{subtitle='subtitles.proyecto_intento.flies';sounds=@(@{name='minecraft:entity.bee.loop';type='event';volume=0.6})}
    beetle_chirp=@{subtitle='subtitles.proyecto_intento.beetle';sounds=@(@{name='minecraft:entity.silverfish.ambient';type='event';pitch=1.35;volume=0.45})}
    beetle_step=@{sounds=@(@{name='minecraft:entity.silverfish.step';type='event';volume=0.5})}
    beetle_roll=@{subtitle='subtitles.proyecto_intento.roll';sounds=@(@{name='minecraft:block.mud.step';type='event';volume=0.6})}
}
foreach($language in @('es_es','es_cl','en_us')) {
    $current=Get-Content (Join-Path $assetDir "lang/$language.json") -Raw -Encoding UTF8 | ConvertFrom-Json
    $strings=@{}; $current.PSObject.Properties | ForEach-Object {$strings[$_.Name]=$_.Value}
    $spanish=$language -ne 'en_us'
    $strings['itemGroup.proyecto_intento.popocraft']='PopoCraft'
    $strings['block.proyecto_intento.popo_pile']=if($spanish){'Montón de popó'}else{'Poop Pile'}
    $strings['entity.proyecto_intento.dung_beetle']=if($spanish){'Escarabajo pelotero'}else{'Dung Beetle'}
    $strings['entity.proyecto_intento.popo_projectile']=if($spanish){'Popó volador'}else{'Flying Poop'}
    $strings['item.proyecto_intento.dung_beetle_spawn_egg']=if($spanish){'Generar escarabajo pelotero'}else{'Dung Beetle Spawn Egg'}
    $strings['effect.proyecto_intento.stinky']=if($spanish){'¡Apestado!'}else{'Stinky!'}
    $strings['subtitles.proyecto_intento.flies']=if($spanish){'Moscas zumban'}else{'Flies buzz'}
    $strings['subtitles.proyecto_intento.beetle']=if($spanish){'Escarabajo chirría'}else{'Beetle chirps'}
    $strings['subtitles.proyecto_intento.roll']=if($spanish){'Escarabajo rueda su bola'}else{'Beetle rolls its ball'}
    Write-Json "assets/proyecto_intento/lang/$language.json" $strings
}
$preview=New-Object Drawing.Bitmap(768,256)
$canvas=[Drawing.Graphics]::FromImage($preview)
$canvas.Clear([Drawing.Color]::FromArgb(110,110,110))
$canvas.InterpolationMode=[Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
$canvas.PixelOffsetMode=[Drawing.Drawing2D.PixelOffsetMode]::Half
for($i=0;$i -lt 4;$i++) {
    $tile=[Drawing.Bitmap]::FromFile((Join-Path $assetDir "textures/item/$($names[$i]).png"))
    $canvas.DrawImage($tile,$i*192,32,192,192); $tile.Dispose()
}
$canvas.Dispose(); $preview.Save((Join-Path $projectDir 'art/armor-icons-v2-preview.png'),[Drawing.Imaging.ImageFormat]::Png); $preview.Dispose()
Write-Output 'Expansion icons, beetle texture, fly particle, pile models, sound events and translations generated.'
