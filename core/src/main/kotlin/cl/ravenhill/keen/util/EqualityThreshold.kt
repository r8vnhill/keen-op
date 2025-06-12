/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.raise.either
import arrow.core.raise.ensure
import cl.ravenhill.keen.exceptions.InvalidThresholdException
import cl.ravenhill.keen.util.EqualityThreshold.Companion.DEFAULT
import cl.ravenhill.keen.util.EqualityThreshold.Companion.EXACT
import cl.ravenhill.keen.util.EqualityThreshold.Companion.RELAXED
import cl.ravenhill.keen.util.EqualityThreshold.Companion.STRICT

/**
 * Represents a validated numerical tolerance used for approximate floating-point equality.
 *
 * This value class encapsulates a non-negative, finite, and non-NaN threshold value.
 * It ensures that approximate equality checks (e.g., `|a - b| <= threshold`) are performed safely and explicitly,
 * helping prevent bugs due to invalid or unintended comparisons.
 *
 * Unlike a raw `Double`, this wrapper enforces constraints on the threshold through controlled instantiation and
 * prevents misuse by making the constructor private.
 *
 * ## Use Cases:
 * - Floating-point equality in constraint solvers.
 * - Numeric optimization and stability checks.
 * - Configurable precision in scientific computation.
 *
 * @property value The absolute difference tolerance. Must be non-negative, finite, and not NaN.
 */
@JvmInline
value class EqualityThreshold private constructor(val value: Double) {

    companion object {
        /** A very strict threshold for high-precision comparisons (e.g., default for constraint solvers). */
        const val STRICT = 1e-9

        /** A more relaxed threshold suitable for lenient evaluations. */
        const val RELAXED = 1e-6

        /** Disables tolerance: values must be exactly equal (not recommended for floating-point). */
        const val EXACT = 0.0

        /** Default threshold used throughout the framework (alias for [STRICT]). */
        const val DEFAULT = STRICT

        /** Predefined validated instance with [STRICT] threshold. */
        val strict = invoke(STRICT)

        /** Predefined validated instance with [RELAXED] threshold. */
        val relaxed = invoke(RELAXED)

        /** Predefined validated instance with [EXACT] threshold (zero tolerance). */
        val exact = invoke(EXACT)

        /** Predefined validated instance with [DEFAULT] threshold. */
        val default = invoke(DEFAULT)

        /**
         * Constructs a validated [EqualityThreshold] from the given [value].
         *
         * ## Validation rules:
         * - Must not be NaN.
         * - Must be finite.
         * - Must be greater than or equal to 0.0.
         *
         * @param value The threshold to validate and wrap.
         * @return A [Right] containing the [EqualityThreshold] if valid, or a [Left] with an
         *   [InvalidThresholdException].
         */
        operator fun invoke(value: Double): Either<InvalidThresholdException, EqualityThreshold> = either {
            ensure(!value.isNaN()) { InvalidThresholdException("Threshold cannot be NaN") }
            ensure(value.isFinite()) { InvalidThresholdException("Threshold should be finite") }
            ensure(value >= 0.0) { InvalidThresholdException("Threshold should be non-negative") }
            EqualityThreshold(value)
        }
    }
}
