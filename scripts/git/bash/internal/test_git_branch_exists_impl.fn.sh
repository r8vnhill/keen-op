#!/usr/bin/env bash

# =============================================================================
# test_git_branch_exists_impl
#
# Checks whether a Git branch exists in the current repository.
# This function supports checking both local and remote branches.
#
# Arguments:
#   $1 - Branch name (required)
#   $2 - Remote name (optional; required if is_remote=1)
#   $3 - is_remote flag (1 for remote branch, 0 for local branch)
#
# Environment:
#   VERBOSE=1      Enables verbose output for debugging
#
# Dependencies:
#   Requires the helper function `get_command_or_else` to be available.
#
# Returns:
#   0 - If the specified branch exists
#   1 - If the branch does not exist or an error occurred
# =============================================================================

# Fail on unset variables, errors, or failed pipelines
set -euo pipefail

# Resolve the path of the current script and source helper
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/get_command_or_else.fn.sh"

# -----------------------------------------------------------------------------
# _normalize_branch_line
#
# Strips leading `*`, leading/trailing whitespace from a git branch line.
# Used to ensure consistent comparison of git branch output.
# -----------------------------------------------------------------------------
_normalize_branch_line() {
  sed -E 's/^\s*(\*)?\s*//;s/\s*$//'
}

# -----------------------------------------------------------------------------
# _log_verbose
#
# Prints to stderr only if VERBOSE=1
# -----------------------------------------------------------------------------
_log_verbose() {
  [[ "${VERBOSE:-0}" -eq 1 ]] && echo "$@" >&2
}

# -----------------------------------------------------------------------------
# test_git_branch_exists_impl
#
# Main function that checks if a git branch exists.
#
# Usage:
#   test_git_branch_exists_impl <branch> <remote> <is_remote>
#
# Returns:
#   0 if the branch exists, 1 otherwise.
# -----------------------------------------------------------------------------
test_git_branch_exists_impl() {
    local branch="$1"
    local remote="$2"
    local is_remote="$3"

    # Ensure a branch name was provided
    if [[ -z "$branch" ]]; then
        echo "❌ Branch name is required." >&2
        return 1
    fi

    # Validate remote value if checking a remote branch
    if [[ "$is_remote" == "1" && -z "$remote" ]]; then
        echo "❌ Remote name is required when is_remote=1" >&2
        return 1
    fi

    # Attempt to resolve the git command or exit with fallback
    local git_cmd
    git_cmd=$(get_command_or_else "git" \
        'echo "❌ Git is required to check for branches." >&2; exit 1'
    ) || return 1

    local target output

    # Compose the git command and collect output
    case "$is_remote" in
        1)
            target="$remote/$branch"
            output=$("$git_cmd" branch -r --list "$target" 2>/dev/null)
            ;;
        0)
            target="$branch"
            output=$("$git_cmd" branch --list "$target" 2>/dev/null)
            ;;
        *)
            echo "❌ Invalid is_remote flag: '$is_remote' (expected 0 or 1)" >&2
            return 1
            ;;
    esac

    # Log the command and result if VERBOSE is set
    _log_verbose "Running: $git_cmd branch ${is_remote:+-r} --list $target"
    _log_verbose "Output: $output"

    # Parse and normalize each line of output, comparing to the expected target
    while IFS= read -r line; do
        local cleaned_line
        cleaned_line="$(echo "$line" | _normalize_branch_line)"
        if [[ "$cleaned_line" == "$target" ]]; then
            return 0  # Branch found
        fi
    done <<< "$output"

    return 1  # Branch not found
}
