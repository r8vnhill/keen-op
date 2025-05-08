/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    id("keen.library")
    id("keen.jvm")
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            // ...
        }

        getByName("jvmMain") {
            // ...
        }

        getByName("jvmTest") {

        }
    }
}
