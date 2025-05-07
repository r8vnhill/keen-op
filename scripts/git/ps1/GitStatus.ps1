#
# Copyright (c) 2025, Ignacio Slater M.
# 2-Clause BSD License.
#

<#
.SYNOPSIS
Displays the current Git status for the repository in the current directory.

.DESCRIPTION
This script wraps the `git status` command to promote consistency, make future extensions easier,
and improve script discoverability as part of a standardized Git command interface.

.EXAMPLE
.\GitStatus.ps1
# Shows the status of the current Git working tree.
#>

[CmdletBinding()]
param()

# Ensure git is available
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Error "❌ 'git' command not found. Make sure Git is installed and available in PATH."
    exit 1
}

$gitArgs = @("status")
if ($PSCmdlet.MyInvocation.BoundParameters["Verbose"]) {
    $gitArgs += "--verbose"
}

Write-Verbose "Running: git $($gitArgs -join ' ')"
git @gitArgs
