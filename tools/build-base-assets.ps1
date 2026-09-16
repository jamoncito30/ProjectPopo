# Repack ImageGen originals into Minecraft's fixed pixel sizes and armor UV layout.
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
$projectDir = Split-Path $PSScriptRoot -Parent
$resourceDir = Join-Path $projectDir 'src/main/resources'
$assetDir = Join-Path $resourceDir 'assets/proyecto_intento'
$dataDir = Join-Path $resourceDir 'data/proyecto_intento'
$utf8 = New-Object System.Text.UTF8Encoding($false)
function Write-Json($relative, $value) {
    $destination = Join-Path $resourceDir $relative
    [IO.Directory]::CreateDirectory((Split-Path $destination)) | Out-Null
    [IO.File]::WriteAllText($destination, ($value | ConvertTo-Json -Depth 30) + "`n", $utf8)
}
function Save-Png($bitmap, $relative) {
    $destination = Join-Path $assetDir $relative
    [IO.Directory]::CreateDirectory((Split-Path $destination)) | Out-Null
    $bitmap.Save($destination, [Drawing.Imaging.ImageFormat]::Png)
}
function Sample-Tile($source, [int]$left, [int]$top, [int]$width, [int]$height, [int]$size) {
    $tile = New-Object Drawing.Bitmap($size, $size)
    for ($y=0; $y -lt $size; $y++) {
        for ($x=0; $x -lt $size; $x++) {
            $pixel = $source.GetPixel([int][Math]::Floor($left + ($x+0.5)*$width/$size), [int][Math]::Floor($top + ($y+0.5)*$height/$size))
            $tile.SetPixel($x, $y, $pixel)
        }
    }
    return ,$tile
}
$tiles = @{}
foreach ($block in @('wet_popo', 'dry_popo')) {
    $source = [Drawing.Bitmap]::FromFile((Join-Path $projectDir "art/source/$block.png"))
    $tile = Sample-Tile $source 0 0 $source.Width $source.Height 16
    Save-Png $tile "textures/block/$block.png"
    $tiles[$block] = $tile
    $source.Dispose()
    Write-Json "assets/proyecto_intento/blockstates/$block.json" @{variants=@{''=@{model="proyecto_intento:block/$block"}}}
    Write-Json "assets/proyecto_intento/models/block/$block.json" @{parent='minecraft:block/cube_all';textures=@{all="proyecto_intento:block/$block"}}
    Write-Json "assets/proyecto_intento/models/item/$block.json" @{parent="proyecto_intento:block/$block"}
    Write-Json "data/proyecto_intento/loot_table/blocks/$block.json" @{type='minecraft:block';pools=@(@{rolls=1;entries=@(@{type='minecraft:item';name="proyecto_intento:$block"});conditions=@(@{condition='minecraft:survives_explosion'})})}
}
$icons = [Drawing.Bitmap]::FromFile((Join-Path $projectDir 'art/source/armor_icons.png'))
$names = @('popo_helmet','popo_chestplate','popo_leggings','popo_boots')
for ($i=0; $i -lt 4; $i++) {
    $tile = Sample-Tile $icons (($i % 2)*$icons.Width/2) ([Math]::Floor($i/2)*$icons.Height/2) ($icons.Width/2) ($icons.Height/2) 16
    Save-Png $tile "textures/item/$($names[$i]).png"
    $tile.Dispose()
    Write-Json "assets/proyecto_intento/models/item/$($names[$i]).json" @{parent='minecraft:item/generated';textures=@{layer0="proyecto_intento:item/$($names[$i])"}}
}
$icons.Dispose()
# Bake the generated material onto vanilla humanoid armor UVs (64x32).
# Layer 1: helmet/chest/arms/boots. Layer 2: waist/leggings.
foreach ($layer in @(1,2)) {
    $armor = New-Object Drawing.Bitmap(64,32)
    for ($y=0; $y -lt 32; $y++) {
        for ($x=0; $x -lt 64; $x++) {
            $used = if ($layer -eq 1) {
                ($x -lt 32 -and $y -lt 16) -or ($x -ge 16 -and $x -lt 56 -and $y -ge 16) -or ($x -lt 16 -and ($y -ge 26 -or ($y -ge 16 -and $y -lt 20)))
            } else { $y -ge 16 -and $x -lt 40 }
            # Open face and a small rim, keeping the player's face visible.
            if ($layer -eq 1 -and $x -ge 9 -and $x -lt 15 -and $y -ge 11 -and $y -lt 16) { $used = $false }
            if ($used) { $armor.SetPixel($x,$y,$tiles['wet_popo'].GetPixel($x % 16,$y % 16)) }
        }
    }
    Save-Png $armor "textures/models/armor/popo_layer_$layer.png"
    $armor.Dispose()
}
Write-Json 'data/proyecto_intento/recipe/wet_popo.json' @{type='minecraft:crafting_shaped';category='building';pattern=@('PPP','PPP','PPP');key=@{P=@{item='proyecto_intento:popo'}};result=@{id='proyecto_intento:wet_popo';count=1}}
Write-Json 'data/proyecto_intento/recipe/dry_popo.json' @{type='minecraft:smelting';category='blocks';ingredient=@{item='proyecto_intento:wet_popo'};result=@{id='proyecto_intento:dry_popo'};experience=0.1;cookingtime=200}
$patterns = @(@('PPP','P P'), @('P P','PPP','PPP'), @('PPP','P P','P P'), @('P P','P P'))
for ($i=0; $i -lt 4; $i++) {
    Write-Json "data/proyecto_intento/recipe/$($names[$i]).json" @{type='minecraft:crafting_shaped';category='equipment';pattern=$patterns[$i];key=@{P=@{item='proyecto_intento:popo'}};result=@{id="proyecto_intento:$($names[$i])";count=1}}
}
# Unlock all recipes from the recipe book as soon as the ingredient is collected.
foreach ($name in (@('wet_popo','dry_popo') + $names)) {
    $ingredient = if ($name -eq 'dry_popo') {'wet_popo'} else {'popo'}
    Write-Json "data/proyecto_intento/advancement/recipes/$name.json" @{parent='minecraft:recipes/root';criteria=@{has_ingredient=@{trigger='minecraft:inventory_changed';conditions=@{items=@(@{items=@("proyecto_intento:$ingredient")})}}};requirements=@(,@('has_ingredient'));rewards=@{recipes=@("proyecto_intento:$name")}}
}
Write-Json 'data/minecraft/tags/block/mineable/shovel.json' @{replace=$false;values=@('proyecto_intento:wet_popo')}
Write-Json 'data/minecraft/tags/block/mineable/pickaxe.json' @{replace=$false;values=@('proyecto_intento:dry_popo')}
Write-Json 'assets/proyecto_intento/lang/es_es.json' @{'item.proyecto_intento.popo'='Popó';'block.proyecto_intento.wet_popo'='Bloque de popó húmedo';'block.proyecto_intento.dry_popo'='Bloque de popó seco';'item.proyecto_intento.popo_helmet'='Casco de popó';'item.proyecto_intento.popo_chestplate'='Pechera de popó';'item.proyecto_intento.popo_leggings'='Grebas de popó';'item.proyecto_intento.popo_boots'='Botas de popó'}
Copy-Item (Join-Path $assetDir 'lang/es_es.json') (Join-Path $assetDir 'lang/es_cl.json')
Write-Json 'assets/proyecto_intento/lang/en_us.json' @{'item.proyecto_intento.popo'='Poop';'block.proyecto_intento.wet_popo'='Wet Poop Block';'block.proyecto_intento.dry_popo'='Dry Poop Block';'item.proyecto_intento.popo_helmet'='Poop Helmet';'item.proyecto_intento.popo_chestplate'='Poop Chestplate';'item.proyecto_intento.popo_leggings'='Poop Leggings';'item.proyecto_intento.popo_boots'='Poop Boots'}
foreach ($tile in $tiles.Values) { $tile.Dispose() }
Write-Output 'Textures, models, recipes, loot tables, advancements, tags and translations generated.'
