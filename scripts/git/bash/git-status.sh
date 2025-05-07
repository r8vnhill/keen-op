#!/usr/bin/env bash
#
# Copyright (c) 2025, Ignacio Slater M.
# 2-Clause BSD License.
#

# ────────────────────────────────────────────────────────────────
# Displays the current Git status for the repository.
#
# This script wraps `git status` to allow consistent invocation,
# future extensibility, and better discoverability across systems.
#
# Usage:
#   ./GitStatus.sh         # normal status
#   ./GitStatus.sh --verbose  # passes --verbose to git
# ────────────────────────────────────────────────────────────────

set -euo pipefail

# Check for git availability
if ! command -v git &> /dev/null; then
  echo "❌ 'git' is not installed or not in PATH." >&2
  exit 1
fi

# Collect git arguments
git_args=("status")

for arg in "$@"; do
  case "$arg" in
    --verbose) git_args+=("--verbose") ;;
    *) echo "⚠️ Unknown argument: $arg" >&2 ;;
  esac
done

echo "📦 Running: git ${git_args[*]}"
git "${git_args[@]}"
