#!/usr/bin/env bash

# Exit on error, undefined variables, or failed pipelines
set -euo pipefail

# =============================================================================
# test_git_remote_branch_exists
#
# Checks whether a given Git branch exists on a specified remote.
# Delegates to the shared implementation: test_git_branch_exists_impl
#
# Arguments:
#   $1 - Branch name (required, without the remote prefix)
#   $2 - Remote name (optional, defaults to 'origin' if omitted)
#
# Environment:
#   VERBOSE=1      Enables verbose logging via _log_verbose
#
# Dependencies:
#   - test_git_branch_exists_impl (sourced from local script)
#
# Returns:
#   0 - if the remote branch exists
#   1 - if it does not exist or an error occurs
#
# Example:
#   test_git_remote_branch_exists "main"            # Checks origin/main
#   test_git_remote_branch_exists "dev" upstream    # Checks upstream/dev
# =============================================================================

# Determine the absolute path of the current script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Source the shared Git branch-check implementation
source "$SCRIPT_DIR/test_git_branch_exists_impl.fn.sh"

# -----------------------------------------------------------------------------
# test_git_remote_branch_exists <branch> [remote]
#
# Checks if a remote Git branch exists.
#
# Parameters:
#   branch - The name of the branch to check (without remote prefix)
#   remote - The name of the remote to check (defaults to 'origin')
#
# Output:
#   Logs errors to stderr and returns 0 if the branch exists, 1 otherwise.
# -----------------------------------------------------------------------------
test_git_remote_branch_exists() {
    local branch="${1:-}"
    local remote="${2:-origin}"

    # Validate branch name
    if [[ -z "$branch" ]]; then
        echo "❌ Branch name is required." >&2
        return 1
    fi

    # Validate remote name
    if [[ -z "$remote" ]]; then
        echo "❌ Remote name is required." >&2
        return 1
    fi

    # Call shared function with is_remote = 1
    test_git_branch_exists_impl "$branch" "$remote" 1
}
