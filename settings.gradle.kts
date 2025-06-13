/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */
// region PLUGIN MANAGEMENT

// Include local builds that define convention and build logic plugins.
//
// This enables the use of precompiled script plugins (e.g., `keen.reproducible`) throughout the project without needing
// to publish them to a remote repository.
pluginManagement {
    includeBuild("build-logic") // Reusable precompiled Gradle plugins for project modules

    repositories {
        mavenCentral()                    // For dependencies from Maven Central
        gradlePluginPortal()              // For resolving external Gradle plugins
    }
}

// endregion

@Suppress("UnstableApiUsage") // Incubating API used for repository mode and dependency resolution config
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS // Forces using only the repositories declared here

    repositories {
        mavenCentral()
    }
}

// region TOOLCHAIN RESOLUTION

// Toolchain resolution support via Foojay API
//
// Adds automatic resolution of JDKs from Foojay when using toolchains.
// Recommended in clean environments or CI where the JDK must be downloaded.
// See: https://docs.gradle.org/current/userguide/toolchains.html#sub:download_repositories
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
// endregion

// Root project name used in logs and outputs
rootProject.name = "keen-op"

// Include project modules
include(":core")
include(":examples")
include(":benchmark")
