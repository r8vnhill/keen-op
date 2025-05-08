/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

// Apply shared conventions and quality tools at the root level.
plugins {
    id("keen.reproducible") // Ensures byte-for-byte reproducible archives
    alias(libs.plugins.kotlin.bin.compatibility) // Kotlin binary compatibility validator
    alias { libs.plugins.detekt }
}

// Configure Kotlin binary compatibility validation
apiValidation {
    ignoredProjects += listOf(
        // Uncomment when needed
        // "test-utils", "examples"
    )
}

subprojects {
    pluginManager.withPlugin("io.gitlab.arturbosch.detekt") {
        tasks.named("detekt").configure {
            dependsOn(
                tasks.withType<io.gitlab.arturbosch.detekt.Detekt>()
                    .matching { it.name != "detekt" }
            )
        }
    }
}
