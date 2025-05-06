// Include the convention plugin build to make its plugins available without publishing them.
// This allows using precompiled script plugins like `keen.reproducible` across all modules.
pluginManagement {
    includeBuild("convention-plugins")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

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
// include("core") // Uncomment or extend as you add modules
