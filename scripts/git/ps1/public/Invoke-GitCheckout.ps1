function Invoke-GitCheckout {
    [Alias("Git-Checkout")]
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
