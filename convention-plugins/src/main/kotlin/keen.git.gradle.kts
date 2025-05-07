/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import tasks.git.GitCommit

plugins {
    id("keen.reproducible")
}


tasks.register<GitCommit>("gitCommit")
