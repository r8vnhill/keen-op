/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    id("keen.library")
    id("keen.jvm")
    alias { libs.plugins.detekt }
}

val detektFormatting = libs.detekt.formatting.get().apply { "$group:$module:$version" }

dependencies {
    detektPlugins(detektFormatting)
}

val kotestBundle = libs.bundles.kotest

kotlin {
    sourceSets {
        getByName("commonMain") {
            // ...
        }

        getByName("jvmMain") {
            // ...
        }

        getByName("jvmTest") {
            implementation(kotestBundle)
        }
    }
}

detekt {
    // Tell Detekt to look at commonMain and jvmMain
    source.setFrom(
        "src/commonMain/kotlin",
        "src/jvmMain/kotlin"
    )
}
