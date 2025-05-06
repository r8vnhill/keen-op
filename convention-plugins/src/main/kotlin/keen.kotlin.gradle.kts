/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

plugins {
    id("keen.reproducible")
    kotlin("multiplatform")
}

//// Apply a specific Java toolchain to ease working on different environments.
//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(21)
//    }
//}
//
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    compilerOptions {
//        jvmTarget.set(JvmTarget.JVM_21)
//        freeCompilerArgs.addAll(listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn"))
//    }
//}
//
//tasks.register<Copy>("installGitHooks") {
//    from(layout.projectDirectory.dir("scripts/git-hooks"))
//    into(layout.projectDirectory.dir(".git/hooks"))
//    fileMode = 0b111101101 // 0755
//}
//
//tasks.getByName("build").dependsOn("installGitHooks")
