/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package tasks.git

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class GitCommit : DefaultTask() {

    init {
        group = "Git"
        description = "Performs a check and commits interactively."
    }

    @TaskAction
    fun run() {
        // 1. Perform a simple check (e.g., ensure there are staged changes)
        val diffCheck = ProcessBuilder("git", "diff", "--cached", "--quiet")
            .directory(project.projectDir)
            .start()
            .waitFor()

        if (diffCheck == 0) {
            logger.lifecycle("✅ No staged changes to commit.")
            return
        }

        // 2. Interactive commit
        logger.lifecycle("📝 Staged changes found. Launching interactive Git commit...")
        val process = ProcessBuilder("git", "commit")
            .directory(project.projectDir)
            .inheritIO() // ensures the user gets the interactive commit experience
            .start()

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("❌ Git commit failed with exit code $exitCode.")
        } else {
            logger.lifecycle("✅ Commit completed.")
        }
    }
}
