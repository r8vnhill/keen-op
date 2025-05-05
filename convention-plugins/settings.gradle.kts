// Sets the name of the root project.
// This is used in logs and directory naming during builds.
rootProject.name = "convention-plugins"

// Configures how Gradle resolves plugins declared in `plugins {}` blocks, such as in build scripts for convention
// plugins.
pluginManagement {
    repositories {
        gradlePluginPortal()    // First, consult Gradle's Plugin Portal (official source for Gradle plugins)
        mavenCentral()          // Fallback to Maven Central for plugins published there
    }
}

@Suppress("UnstableApiUsage") // Suppresses warnings for incubating APIs used below
dependencyResolutionManagement {
    // Enforce repositories declared here over any declared in individual build scripts.
    // This avoids duplication and makes repository resolution predictable.
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Incubating API

    // Repositories used to resolve dependencies declared in build scripts.
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    // Declares a shared version catalog named `libs`, accessible via `libs.<alias>` in build scripts.
    // This catalog centralizes dependency and plugin versions across the project.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
