/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    id("keen.reproducible")
    alias { libs.plugins.kotlin.bin.compatibility }
    alias { libs.plugins.detekt }
}

val detektId = libs.plugins.detekt.get().pluginId
val detektFormattingModule = libs.detekt.formatting.get().module.toString()
val detektFormattingVersion = libs.detekt.formatting.get().version

subprojects {
    apply(plugin = detektId)

    dependencies {
        detektPlugins("$detektFormattingModule:$detektFormattingVersion")
    }
}

apiValidation {
    ignoredProjects += listOf(
        //"test-utils", "examples"
    )
}
