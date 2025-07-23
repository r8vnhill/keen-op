# Load all public functions in a robust and controlled manner
$publicScripts = Get-ChildItem `
    -Path (Join-Path -Path $PSScriptRoot -ChildPath 'public') `
    -Filter '*.ps1' `
    -File `
    -ErrorAction Stop

foreach ($script in $publicScripts) {
    try {
        . $script.FullName
    } catch {
        Write-Warning "⚠️ Failed to import $($script.Name): $_"
    }
}
