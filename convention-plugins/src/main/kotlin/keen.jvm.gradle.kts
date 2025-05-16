/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import utils.JvmToolchain.setDefaultJavaVersion

// region : SHARED KOTLIN BUILD CONFIGURATION ──────────────────────────────────────────────────────────────────────────
// Apply shared Kotlin build configuration via convention plugin.
plugins {
    id("keen.reproducible") // Custom plugin to ensure build reproducibility (e.g., consistent archive output)
    kotlin("jvm")
}
// endregion ──────────────────────────────────────────────────────────────────────────────────────────────────────── //

// region : JAVA TOOLCHAIN ─────────────────────────────────────────────────────────────────────────────────────────────
// Configure Java toolchain for consistency across environments.
//
// Ensures that both Java and Kotlin compilers use the same Java version, as defined in the shared [JvmToolchain]
// utility.
java.toolchain {
    setDefaultJavaVersion() // Applies the default Java version (e.g., Java 22)
}

kotlin.jvmToolchain {
    setDefaultJavaVersion() // Applies the same version for Kotlin JVM compilation
}
// endregion ──────────────────────────────────────────────────────────────────────────────────────────────────────── //

// region : KOTLIN SETTINGS ────────────────────────────────────────────────────────────────────────────────────────────
// Configures Kotlin Multiplatform-specific settings.
kotlin {
    // Additional compiler options for stricter and experimental features
    compilerOptions {
        @Suppress("SpellCheckingInspection")
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn", // Enables usage of opt-in APIs
            "-Xcontext-parameters"                      // Enables context parameters
        )
    }

    // Apply language settings to all source sets
    sourceSets.all {
        languageSettings {
            optIn("cl.ravenhill.keen.ExperimentalKeen")    // Enables use of experimental Keen APIs
            optIn("io.kotest.common.ExperimentalKotest")   // Enables use of experimental Kotest APIs
        }
    }
}
// endregion ──────────────────────────────────────────────────────────────────────────────────────────────────────── //
