/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.matchers

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import kotlin.math.abs

private fun beCloseTo(
    expected: Double,
    tolerance: Double = 1e-9
): Matcher<Double> {
    require(expected.isFinite()) { "Expected value must be finite, but was $expected." }
    require(tolerance >= 0.0) { "Tolerance must be non-negative, but was $tolerance." }

    return Matcher { actual ->
        val diff = abs(actual - expected)
        MatcherResult(
            passed = diff <= tolerance,
            failureMessageFn = {
                "Expected %.6f to be close to %.6f within a tolerance of %.1e (actual diff: %.2e)"
                    .format(actual, expected, tolerance, diff)
            },
            negatedFailureMessageFn = {
                "Expected %.6f not to be close to %.6f within a tolerance of %.1e"
                    .format(actual, expected, tolerance)
            }
        )
    }
}

fun Double.shouldBeCloseTo(
    expected: Double,
    tolerance: Double = 1e-9
): Double = apply { should(beCloseTo(expected, tolerance)) }
