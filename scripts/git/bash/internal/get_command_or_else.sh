#!/usr/bin/env bash

# -----------------------------------------------------------------------------
# get_command_or_else
#
# Checks if a command exists in the current environment. If it exists, prints
# the command name (or full path) and returns success. If it does not exist,
# runs a fallback function or command string provided by the caller.
#
# The fallback is executed only if the command is missing. You can pass either:
#   - The name of a defined function
#   - An inline shell command (e.g., 'echo "install it"; exit 1')
#
# Usage:
#   get_command_or_else "git" fallback_function
#   get_command_or_else "node" 'echo "Please install Node.js"; exit 1'
#
# Returns:
#   0 if the command exists
#   non-zero if fallback was triggered and fails
# -----------------------------------------------------------------------------

get_command_or_else() {
    # Ensure the function receives at least two arguments
    if [[ $# -lt 2 ]]; then
        echo "❌ Usage: get_command_or_else <command> <fallback>" >&2
        return 2
    fi

    local cmd="$1"
    local fallback="$2"

    # Check if the command exists
    if command -v "$cmd" >/dev/null 2>&1; then
        echo "$cmd"
        return 0
    fi

    # Print warning and execute fallback logic
    echo "⚠️ Command '$cmd' not found. Executing fallback: $fallback" >&2

    if declare -F "$fallback" >/dev/null 2>&1; then
        # Fallback is a named function
        "$fallback"
    else
        # Fallback is an inline shell command
        eval "$fallback"
    fi

    return $? # Return the exit status of the fallback command or function
}
