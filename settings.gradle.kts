// region : PLUGIN MANAGEMENT ─────────────────────────────────────────────-──────────────────────────────-─────────────
// Include local builds that define convention and build logic plugins.
//
// This enables the use of precompiled script plugins (e.g., `keen.reproducible`) throughout the project without needing
// to publish them to a remote repository.
pluginManagement {
    includeBuild("convention-plugins")    // Reusable precompiled Gradle plugins for project modules

    repositories {
        mavenCentral()                    // For dependencies from Maven Central
        gradlePluginPortal()              // For resolving external Gradle plugins
    }
}
// endregion ───────────────────────────────────────────────────────────────-──────────────────────────────-───────── //

// Automatically resolves the required JDK using Foojay (https://foojay.io).
// Ensures consistent Java toolchains across environments without manual setup.
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

@Suppress("UnstableApiUsage") // Incubating API used for repository mode and dependency resolution config
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS // Forces using only the repositories declared here

    repositories {
        mavenCentral()
    }
}

// Root project name used in logs and outputs
rootProject.name = "keen-go"

// Include project modules

