/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    `java-library`
}

testing {
    suites {
        @Suppress("UnstableApiUsage")
        getting(JvmTestSuite::class) {  // Incubating API
            useJUnitJupiter() // Incubating API
        }
    }
}
