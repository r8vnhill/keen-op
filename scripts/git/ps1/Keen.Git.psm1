# Load all public functions
Get-ChildItem -Path "$PSScriptRoot/public/*.ps1" | ForEach-Object {
    . $_.FullName
}
