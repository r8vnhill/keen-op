pluginManagement {
    includeBuild("convention-plugins")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

@Suppress("UnstableApiUsage") // Suppresses warnings for incubating APIs used below
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS // Incubating API

    repositories {  // Incubating API
        mavenCentral()
    }
}

rootProject.name = "keen-go"

// Include project modules
//include("core")
