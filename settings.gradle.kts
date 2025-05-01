// Automatically resolve JDKs via Foojay
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "keen-go"

// Include project modules
include("lib")
