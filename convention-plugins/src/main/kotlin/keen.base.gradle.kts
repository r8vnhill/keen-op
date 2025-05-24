/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

// region : Apply a consistent version to all subprojects ──────────────────────────────────────────────────────────────

// This ensures that every module in the multi-module build uses the same release version, sourced from the centralized
// [Versions] object.
subprojects {
    // Set the project version for each submodule to the latest stable release
    project.version = Versions.latest
}
// endregion ───────────────────────────────────────────────────────────────────────────────────────────────────────────
