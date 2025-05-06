/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

tasks.withType<Test>().forEach {
    it.useJUnitPlatform()
    it.testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
