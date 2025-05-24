<#
.SYNOPSIS
Checks whether a remote Git branch exists on the 'origin' remote.

.DESCRIPTION
This function verifies if a branch with the specified name exists on the remote `origin`.
It uses `git branch -r --list` to query the list of remote branches and returns `$true` if a match is found, or `$false` otherwise.
If Git is unavailable or an error occurs during execution, the function catches the error and returns `$false`.

This can be used in scripts that depend on the existence of a remote branch, such as conditional deployments, release tagging, or automation flows.

.PARAMETER BranchName
The name of the remote branch to check for (without the `origin/` prefix).

.EXAMPLE
Test-GitRemoteBranchExists "main"

Returns `$true` if `origin/main` exists on the remote, otherwise `$false`.

.EXAMPLE
if (Test-GitRemoteBranchExists "release/v1.0.0") {
    Write-Host "Branch already exists."
} else {
    Write-Host "Creating release branch..."
}

.OUTPUTS
[bool] – `$true` if the branch exists remotely, `$false` otherwise.

.NOTES
- Requires Git to be installed and available in PATH.
- This function assumes the remote is named `origin`.
#>
function Test-GitRemoteBranchExists {
    [CmdletBinding()]
    [OutputType([bool])]
    param (
        [Parameter(Mandatory, Position = 0)]
        [string]$BranchName
    )

    try {
        $remoteBranch = git branch -r --list "origin/$BranchName"
        return -not [string]::IsNullOrWhiteSpace($remoteBranch)
    } catch {
        Write-Verbose "Failed to check remote branch: $_"
        return $false
    }
}
