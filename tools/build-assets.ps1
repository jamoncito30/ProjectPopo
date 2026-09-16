# Public entrypoint: regenerate the complete mod, including the expansion.
$ErrorActionPreference = 'Stop'
& (Join-Path $PSScriptRoot 'build-expansion-assets.ps1')
& (Join-Path $PSScriptRoot 'build-nest-assets.ps1')
& (Join-Path $PSScriptRoot 'build-alchemy-assets.ps1')
python (Join-Path $PSScriptRoot 'build-grand-nest-assets.py')
if ($LASTEXITCODE -ne 0) { throw 'Grand nest asset generation failed' }
& (Join-Path $PSScriptRoot 'build-roadmap-assets.ps1')
