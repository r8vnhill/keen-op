#!/usr/bin/env bash

# -----------------------------------------------------------------------------
# test_git_branch_exists
#
# Checks if a local Git branch exists in the current repository.
#
# Arguments:
#   $1 - Branch name (required)
#
# Returns:
#   0 if the branch exists
#   1 if it does not or an error occurs
# -----------------------------------------------------------------------------

set -euo pipefail

# Resolve the path of the current script and source helper
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/test_git_branch_exists_impl.fn.sh"

test_git_branch_exists() {
    local branch="$1"

    if [[ -z "${1:-}" ]]; then
        echo "❌ Branch name is required." >&2
        return 1
    fi

    # Delegate to implementation (local branch = is_remote=0)
    test_git_branch_exists_impl "$branch" "" 0
}
