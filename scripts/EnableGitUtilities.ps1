[CmdletBinding()]
param ()

$modulePath = Join-Path -Path $PSScriptRoot -ChildPath 'git\ps1\Keen.Git.psd1'

if (-not (Test-Path $modulePath)) {
    Write-Error "❌ Module not found at path: $modulePath"
    return
}

try {
    Import-Module -Name $modulePath -Force -ErrorAction Stop
    Write-Verbose "✅ Module imported successfully from: $modulePath"
} catch {
    Write-Error "❌ Failed to import module: $_"
}
