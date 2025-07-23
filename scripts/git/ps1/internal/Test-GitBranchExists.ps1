. $PSScriptRoot\Get-CommandOrElse.ps1

<#
.SYNOPSIS
Checks whether a Git branch exists locally.

.DESCRIPTION
This function verifies the existence of a local Git branch by calling `git branch --list <branch>`.
If the `git` command is not found in the current environment, a clear error is thrown via a fallback script block.
The function returns `$true` if the branch is found, and `$false` otherwise.

This is useful in build scripts or version control workflows where conditional logic depends on branch existence.

.PARAMETER BranchName
The name of the Git branch to check for.
Must be a valid local branch name.

.OUTPUTS
[bool]  
Returns `$true` if the branch exists locally, otherwise `$false`.

.EXAMPLE
Test-GitBranchExists -BranchName 'main'

Returns `$true` if a branch named `main` exists in the local repository.

.NOTES
Relies on the helper function `Get-CommandOrElse` to ensure the `git` command is available.
#>
function Test-GitBranchExists {
    [CmdletBinding()]
    [OutputType([bool])]
    param (
        [Parameter(Mandatory = $true)]
        [string]
        $BranchName
    )

    try {
        # Attempt to get the 'git' command or fail with clear message
        $git = Get-CommandOrElse 'git' {
            throw '❌ Git is required to check for branches.'
        }

        # Compose the arguments
        $gitArgs = @('branch', '--list', $BranchName)
        Write-Verbose "Running: git $($gitArgs -join ' ')"

        # Execute git and capture output
        $output = & $git @gitArgs

        # Define strict match pattern (trailing/leading spaces, optional '*')
        $pattern = "^\s*(\*?\s*)?$([Regex]::Escape($BranchName))\s*$"

        # Evaluate branch existence using regex
        if ($output -match $pattern) {
            return $true
        } else {
            return $false
        }
    } catch {
        Write-Error "An error occurred while checking for the branch: $_"
        return $false
    }
}
