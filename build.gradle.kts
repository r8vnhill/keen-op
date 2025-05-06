/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

// Apply shared conventions and quality tools at the root level.
plugins {
    id("keen.git")
    id("keen.reproducible") // Ensures byte-for-byte reproducible archives
    alias(libs.plugins.kotlin.bin.compatibility) // Kotlin binary compatibility validator
    alias(libs.plugins.detekt) // Static analysis for Kotlin code
}

// Extract plugin and version info from the version catalog to avoid hardcoding.
val detektId = libs.plugins.detekt.get().pluginId
val detektFormattingModule = libs.detekt.formatting.get().module.toString()
val detektFormattingVersion = libs.detekt.formatting.get().version

subprojects {
    // Apply Detekt plugin to all subprojects
    apply(plugin = detektId)

    // Configure Detekt formatting plugin
    dependencies {
        detektPlugins("$detektFormattingModule:$detektFormattingVersion")
    }
}

// Configure Kotlin binary compatibility validation
apiValidation {
    ignoredProjects += listOf(
        // Uncomment when needed
        // "test-utils", "examples"
    )
}
