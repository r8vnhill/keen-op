/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Holds centralized version properties for the project, each validated against Semantic Versioning
 * (`MAJOR.MINOR.PATCH`) with optional `-SNAPSHOT` suffix.
 *
 * The versions are provided via [validateSemanticVersion], which ensures that any assigned value matches the semantic
 * version pattern and throws an exception otherwise.
 *
 * ## Usage:
 * ```kotlin
 * // Access the latest stable release version
 * val release = Versions.latest
 *
 * // Access the initial release version
 * val start = Versions.initial
 *
 * // Access the latest development snapshot version
 * val dev = Versions.latestDev
 * ```
 *
 * @property latest    The most recent published release version (e.g., "0.1.0").
 * @property initial   The version at the start of development (e.g., "0.1.0").
 * @property latestDev The current development snapshot version (e.g., "0.1.0-SNAPSHOT").
 */
@Suppress("unused")
object Versions {
    /** The latest published version of the project, validated on first access. */
    val latest by validateSemanticVersion { "0.1.0" }

    /** Initial development version, validated on first access. */
    val initial by validateSemanticVersion { "0.1.0" }

    /** Latest development version with `-SNAPSHOT` suffix, validated on first access. */
    val latestDev by validateSemanticVersion { "0.1.0-SNAPSHOT" }
}

/**
 * Creates a read-only property delegate that enforces semantic version validation on the assigned value.
 *
 * The delegate invokes the provided [block] once to get the version string, then checks that it matches the pattern
 * `MAJOR.MINOR.PATCH` with an optional `-SNAPSHOT` suffix.
 * If the validation fails, an [IllegalArgumentException] is thrown indicating the property name and invalid value.
 *
 * ## Usage:
 * ```kotlin
 * class ProjectConfig {
 *     // Validates at first access that the returned string is a semantic version.
 *     val releaseVersion: String by validateSemanticVersion { "1.2.3" }
 *
 *     // Allows snapshot suffix.
 *     val snapshotVersion: String by validateSemanticVersion { "2.0.0-SNAPSHOT" }
 * }
 * ```
 *
 * @param block Lambda that produces the version string to validate.
 * @return A [ReadOnlyProperty] delegate that returns the validated version or throws if invalid.
 */
fun validateSemanticVersion(block: () -> String): ReadOnlyProperty<Any?, String> =
    object : ReadOnlyProperty<Any?, String> {
        // Regex matching semantic versions like "1.0.0" or "1.0.0-SNAPSHOT"
        private val regex = Regex("""\d+\.\d+\.\d+(-SNAPSHOT)?""")
        // Lazily compute the version value on first access
        private val value by lazy(block)

        override fun getValue(thisRef: Any?, property: KProperty<*>): String {
            require(regex.matches(value)) {
                "Version '$value' assigned to '${property.name}' is not a valid semantic version."
            }
            return value
        }
    }
