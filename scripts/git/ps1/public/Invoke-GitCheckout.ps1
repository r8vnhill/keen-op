function Invoke-GitCheckout {
    [CmdletBinding()]
    param (
        [Parameter(Mandatory, Position = 0)]
        [string]$BranchName
    )

    Ensure-GitAvailable

    $gitArgs = Get-GitCheckoutArgs -BranchName $BranchName

    if ($PSCmdlet.MyInvocation.BoundParameters.ContainsKey("Verbose")) {
        $gitArgs += "--verbose"
    }

    Write-Verbose "Running: git $($gitArgs -join ' ')"

    try {
        git @gitArgs | Write-Verbose
        Write-Host "✅ Successfully checked out to branch '$BranchName'."
    } catch {
        Write-Error "❌ Git checkout failed: $_"
    }
}

function Ensure-GitAvailable {
    if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
        throw "❌ 'git' command not found. Make sure Git is installed and available in PATH."
    }
}

function Get-GitCheckoutArgs {
    param (
        [string]$BranchName
    )

    if (Test-GitBranchExists -BranchName $BranchName) {
        return @("checkout", $BranchName)
    }

    Write-Verbose "Branch '$BranchName' not found locally. Fetching remotes..."
    git fetch --all | Out-Null

    if (-not (Test-GitRemoteBranchExists -BranchName $BranchName)) {
        throw "❌ Branch '$BranchName' not found locally or remotely."
    }

    return @("checkout", "--track", "origin/$BranchName")
}

function Test-GitBranchExists {
    param ([string]$BranchName)
    return [bool](git branch --list $BranchName)
}

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
function script:Test-GitRemoteBranchExists {
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
