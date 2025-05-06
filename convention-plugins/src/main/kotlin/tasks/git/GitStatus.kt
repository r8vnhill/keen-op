/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package tasks.git

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Gradle task that executes `git status` and prints its output.
 *
 * Implements [GitCommand] to allow direct invocation as a function, and uses a `@TaskAction` to print the status as
 * part of the Gradle build.
 *
 * ## Usage:
 * Add this task to your `build.gradle.kts`:
 *
 * ```kotlin
 * tasks.register<GitStatus>("gitStatus")
 * ```
 *
 * Then run:
 * ```shell
 * ./gradlew gitStatus
 * ```
 *
 * ### Example 1: Direct invocation as function
 * ```kotlin
 * val status = GitStatus()
 * println(status()) // Prints the output of `git status`
 * ```
 * @property project Inherited from [DefaultTask]; defines the working directory for the Git command.
 * @see [GitCommand]
 */
abstract class GitStatus : DefaultTask(), GitCommand {

    init {
        group = "Git"
        description = "Prints the output of `git status`."
    }

    /**
     * Executes the `git status` command in the root directory of the current Gradle project.
     *
     * The command is run using a [ProcessBuilder], capturing and returning its standard output.
     * If the command exits with a non-zero code, a [RuntimeException] is thrown.
     *
     * @return The standard output of the `git status` command.
     * @throws RuntimeException if the command fails (non-zero exit code).
     */
    override fun invoke(): String {
        val process = ProcessBuilder("git", "status")
            .directory(project.projectDir)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream.bufferedReader().use { it.readText() }

        if (process.waitFor() != 0) {
            throw RuntimeException("Git command failed with exit code ${process.exitValue()}")
        }

        return output
    }

    /**
     * Runs the `git status` command and logs its output to the Gradle lifecycle logger.
     *
     * This method is invoked automatically when the `GitStatus` task is executed via Gradle.
     * It delegates to [invoke] to perform the actual Git command execution.
     *
     * @see invoke
     */
    @TaskAction
    fun run() {
        val output = invoke()
        logger.lifecycle(output)
    }
}
