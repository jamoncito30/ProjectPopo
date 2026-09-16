# Code-native block geometry; reuses the existing wet/dry textures.
$ErrorActionPreference = 'Stop'
$resourceRoot = Join-Path (Split-Path $PSScriptRoot -Parent) 'src/main/resources'
function Write-NestJson($relative, $value) {
    $destination = Join-Path $resourceRoot $relative
    [IO.Directory]::CreateDirectory((Split-Path $destination -Parent)) | Out-Null
    [IO.File]::WriteAllText($destination, ($value | ConvertTo-Json -Depth 30), [Text.UTF8Encoding]::new($false))
}
function Nest-Cube($from, $to, $texture) {
    $faces = @{}
    foreach ($face in @('up','down','north','south','east','west')) { $faces[$face] = @{texture=$texture} }
    return @{from=$from;to=$to;faces=$faces}
}
$variants = @{}
for ($stage=1; $stage -le 5; $stage++) {
    $parts = @((Nest-Cube @(1,0,1) @(15,2,15) '#wet'))
    if ($stage -eq 1) { $parts += Nest-Cube @(4,2,4) @(12,4,12) '#wet' }
    if ($stage -ge 2) {
        $height = [Math]::Min(10, 2 + $stage * 2)
        # North-facing doorway stays visibly open between the side walls.
        $parts += Nest-Cube @(2,2,3) @(5,$height,14) '#dry'
        $parts += Nest-Cube @(11,2,3) @(14,$height,14) '#dry'
        $parts += Nest-Cube @(5,2,11) @(11,$height,14) '#wet'
    }
    if ($stage -ge 3) { $parts += Nest-Cube @(3,7,7) @(13,8,13) '#dry' }
    if ($stage -ge 4) { $parts += Nest-Cube @(3,8,2) @(13,10,12) '#dry' }
    if ($stage -eq 5) {
        $parts += Nest-Cube @(5,10,4) @(11,12,11) '#dry'
        $parts += Nest-Cube @(4,2,1) @(5,7,3) '#wet'
        $parts += Nest-Cube @(11,2,1) @(12,7,3) '#wet'
        $parts += Nest-Cube @(4,7,1) @(12,8,3) '#wet'
    }
    Write-NestJson "assets/proyecto_intento/models/block/beetle_nest_$stage.json" @{
        parent='minecraft:block/block';textures=@{wet='proyecto_intento:block/wet_popo';dry='proyecto_intento:block/dry_popo';particle='proyecto_intento:block/dry_popo'};elements=$parts
    }
    $variants["stage=$stage"] = @{model="proyecto_intento:block/beetle_nest_$stage"}
}
Write-NestJson 'assets/proyecto_intento/blockstates/beetle_nest.json' @{variants=$variants}
Write-NestJson 'assets/proyecto_intento/models/item/beetle_nest.json' @{parent='proyecto_intento:block/beetle_nest_5'}
foreach ($language in @('es_es','es_cl','en_us')) {
    $current = Get-Content -Raw -Encoding UTF8 (Join-Path $resourceRoot "assets/proyecto_intento/lang/$language.json") | ConvertFrom-Json
    $strings = @{}; $current.PSObject.Properties | ForEach-Object { $strings[$_.Name]=$_.Value }
    $strings['block.proyecto_intento.beetle_nest'] = if ($language -eq 'en_us') {'Dung Beetle Shelter'} else {'Refugio de escarabajos'}
    Write-NestJson "assets/proyecto_intento/lang/$language.json" $strings
}
Write-NestJson 'data/minecraft/tags/block/mineable/shovel.json' @{replace=$false;values=@('proyecto_intento:wet_popo','proyecto_intento:popo_pile','proyecto_intento:beetle_nest')}
Write-Output 'Five shelter stages, item model, translations and shovel tag generated.'
