/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

// ───────────────────────────────────────────────────────────────
// Applies essential plugins for reproducibility and Kotlin MPP.
// ───────────────────────────────────────────────────────────────
plugins {
    id("keen.reproducible") // Custom plugin to ensure build reproducibility (e.g., consistent archive output)
    kotlin("multiplatform") // Enables support for Kotlin Multiplatform projects
}

// ───────────────────────────────────────────────────────────────
// Configures Kotlin Multiplatform-specific settings.
// ───────────────────────────────────────────────────────────────
kotlin {
    // Additional compiler options for stricter and experimental features
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",      // Allows expect/actual classes across platforms
            "Xjsr305=strict",               // Enforces nullability annotations for Java interop
            "-opt-in=kotlin.RequiresOptIn"  // Enables usage of opt-in APIs
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
