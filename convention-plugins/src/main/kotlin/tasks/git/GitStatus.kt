/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package tasks.git

import org.gradle.api.DefaultTask

class GitStatus : DefaultTask(), GitCommand {
    override fun invoke(): String {
        val process = ProcessBuilder("git", "status")
            .directory(project.projectDir)
            .redirectErrorStream(true)
            .start()

        return process.inputStream.bufferedReader().readText()
    }
}
