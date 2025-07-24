function Invoke-GitSync {
    [Alias('Git-Sync')]
    [CmdletBinding()]
    param (
        [switch] $Push,
        [switch] $PassThru
    )

    # Load helper functions
    . "$PSScriptRoot/../internal/Get-CommandOrElse.ps1"
    . "$PSScriptRoot/../internal/Get-CurrentBranchName.ps1"

    # Resolve the git command or fail with a helpful error
    $git = Get-CommandOrElse -Command git -Else {
        throw '❌ Git is required to perform sync operations.'
    }

    Write-Verbose "📥 Fetching all remotes..."
    & $git fetch --all
    if ($LASTEXITCODE -ne 0) {
        throw "❌ Failed to fetch from remote."
    }

    $currentBranch = Get-CurrentBranchName
    if (-not $currentBranch) {
        throw "❌ Could not determine the current branch."
    }

    Write-Verbose "🔀 Merging origin/$currentBranch into $currentBranch..."
    & $git merge "origin/$currentBranch"
    if ($LASTEXITCODE -ne 0) {
        throw "❌ Merge failed. Please resolve conflicts manually."
    }

    if ($Push.IsPresent) {
        Write-Verbose "📤 Pushing $currentBranch to origin..."
        & $git push origin $currentBranch
        if ($LASTEXITCODE -ne 0) {
            throw "❌ Push failed. Please check remote status."
        }
    }

    if ($PassThru.IsPresent) {
        Write-Output "✅ Synced branch '$currentBranch' with remote origin."
    }
}
