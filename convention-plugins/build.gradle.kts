// Apply the Kotlin DSL plugin to support writing build logic in Kotlin.
// This is required for convention plugin projects and other precompiled script plugins.
plugins {
    `kotlin-dsl`
}

dependencies {
    // Provides access to Kotlin-specific Gradle plugin APIs
    implementation(libs.kotlin.gradle.plugin)
}
