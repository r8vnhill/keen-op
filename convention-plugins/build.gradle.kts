// Apply the Kotlin DSL plugin to support writing build logic in Kotlin.
// This is required for convention plugin projects and other precompiled script plugins.
plugins {
    `kotlin-dsl`
}

dependencies {
    // Provides access to Kotlin-specific Gradle plugin APIs
    compileOnly(libs.kotlin.gradle.plugin) // Use compileOnly to avoid including the plugin in the build output
}

// Configure all archive-producing tasks (Jar, Zip, Tar, etc.) to support reproducible builds.
// This makes build outputs stable and byte-for-byte identical across machines and over time, which improves caching,
// enables integrity verification, and supports efforts like https://reproducible-builds.org.
// See: https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
tasks.withType<AbstractArchiveTask>().configureEach {
    // Prevents file timestamps from being recorded in the archive, so the output doesn't change just because the files
    // were built at a different time.
    isPreserveFileTimestamps = false

    // Ensures files are added to the archive in a consistent, deterministic order, regardless of how the underlying
    // filesystem lists them.
    isReproducibleFileOrder = true
}
