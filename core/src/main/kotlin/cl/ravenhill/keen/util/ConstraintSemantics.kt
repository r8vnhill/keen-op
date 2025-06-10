/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util

import cl.ravenhill.keen.problem.InequalityConstraint

/**
 * Represents the type of inequality used in an [InequalityConstraint].
 *
 * Each enum entry corresponds to a relational operator and provides a human-readable [symbol] for logging, debugging,
 * or report generation.
 *
 * ## Example:
 * ```kotlin
 * val type = InequalityType.LESS_THAN
 * println(type.symbol) // prints "<"
 * ```
 */
enum class InequalityType {
    /** Represents the `<` relation. */
    LESS_THAN,

    /** Represents the `>` relation. */
    GREATER_THAN,

    /** Represents the `<=` relation. */
    LESS_THAN_OR_EQUAL,

    /** Represents the `>=` relation. */
    GREATER_THAN_OR_EQUAL;

    /** A symbolic representation of the inequality type (e.g., "<="). */
    val symbol: String
        get() = when (this) {
            LESS_THAN -> "<"
            GREATER_THAN -> ">"
            LESS_THAN_OR_EQUAL -> "<="
            GREATER_THAN_OR_EQUAL -> ">="
        }
}
