#!/usr/bin/env bash

git_checkout() {
  local branch=""
  local remote="origin"
  local pass_thru=0
  local extra_args=()

  _parse_git_checkout_args "$@" || return $?
  branch="${GIT_BRANCH}"
  remote="${GIT_REMOTE}"
  pass_thru="${GIT_PASS_THRU}"
  extra_args=("${GIT_EXTRA_ARGS[@]}")

  if [[ -z "$branch" ]]; then
    _git_checkout_usage
    return 2
  fi

  _log_checkout_summary "$branch" "$remote" "$pass_thru" "${extra_args[@]}"

  git checkout "$branch" "${extra_args[@]}"

  if [[ "$pass_thru" -eq 1 ]]; then
    echo "✅ Checkout to '$branch' completed"
  fi
}

# -----------------------------------------------------------------------------
# _parse_git_checkout_args "$@"
# Parses input arguments into global variables:
# - GIT_BRANCH, GIT_REMOTE, GIT_PASS_THRU, GIT_EXTRA_ARGS[]
# -----------------------------------------------------------------------------
_parse_git_checkout_args() {
  GIT_BRANCH=""
  GIT_REMOTE="origin"
  GIT_PASS_THRU=0
  GIT_EXTRA_ARGS=()

  while [[ $# -gt 0 ]]; do
    case "$1" in
      --remote)
        GIT_REMOTE="$2"
        shift 2
        ;;
      --pass-thru)
        GIT_PASS_THRU=1
        shift
        ;;
      --*)
        echo "❌ Unknown option: $1" >&2
        return 1
        ;;
      *)
        if [[ -z "$GIT_BRANCH" ]]; then
          GIT_BRANCH="$1"
        else
          GIT_EXTRA_ARGS+=("$1")
        fi
        shift
        ;;
    esac
  done
}

# -----------------------------------------------------------------------------
# _git_checkout_usage
# Displays usage info.
# -----------------------------------------------------------------------------
_git_checkout_usage() {
  echo "❌ Missing required branch name." >&2
  echo "Usage: git_checkout <branch> [--remote origin] [--pass-thru] [extra git args...]" >&2
}

# -----------------------------------------------------------------------------
# _log_checkout_summary <branch> <remote> <pass_thru> [extra args...]
# Logs what is about to be done.
# -----------------------------------------------------------------------------
_log_checkout_summary() {
  local branch="$1"
  local remote="$2"
  local pass_thru="$3"
  shift 3
  local extra_args=("$@")

  echo "📦 Branch     : $branch"
  echo "🌐 Remote     : $remote"
  echo "🔁 Pass-Thru  : $pass_thru"
  echo "➕ Extra Args : ${extra_args[*]}"
}
