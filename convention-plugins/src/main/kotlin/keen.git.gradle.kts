/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import tasks.git.GitStatus

plugins {
    id("keen.reproducible")
}

tasks.register<GitStatus>("gitStatus")
