/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.problem.constrained

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import arrow.core.tail
import cl.ravenhill.keen.Solution
import cl.ravenhill.keen.exceptions.InvalidThresholdException
import cl.ravenhill.keen.matchers.shouldBeCloseTo
import cl.ravenhill.keen.util.EqualityThreshold
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.property.Arb
import io.kotest.property.Gen
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class EqualityConstraintTest : FreeSpec({
    "An 'Equality Constraint' when" - {
        "creating an instance" - {
            "in the context of" - {
                "a valid equality threshold should" - {
                    with(Validity.AllValid) {
                        "bind the threshold to the constraint" {
                            checkAll(
                                arbEqualityThreshold(), // Valid thresholds
                                arbFunction(),  // Functions to apply to the solution
                                arbFunction()   // Functions to apply to the solution
                            ) { threshold, left, right ->
                                with(threshold.right()) {
                                    EqualityConstraint(left, right)
                                        .shouldBeRight()
                                        .threshold
                                        .value
                                        .shouldBeCloseTo(threshold.value)
                                }
                            }
                        }
                    }
                }
                "an invalid equality threshold should" - {
                    with(Validity.AllInvalid) {
                        "return a Left with an InvalidThresholdException" {
                            checkAll(
                                arbEqualityThreshold(),
                                arbFunction(),
                                arbFunction()
                            ) { invalidThreshold, left, right ->
                                with(invalidThreshold.left()) {
                                    EqualityConstraint(left, right)
                                        .shouldBeLeft()
                                        .shouldHaveMessage(
                                            invalidThreshold
                                                .message
                                                .shouldNotBeNull {
                                                    "Invalid threshold message should not be null, this is probably a " +
                                                            "bug on the test setup"
                                                }
                                        )
                                }
                            }
                        }
                    }
                }
            }
            "with default threshold" - {
                "provided by the framework should" - {
                    "create a valid constraint" {
                        checkAll(
                            arbFunction(),  // Functions to apply to the solution
                            arbFunction()   // Functions to apply to the solution
                        ) { left, right ->
                            EqualityConstraint.withDefaultThreshold(left, right)
                                .shouldBeRight()
                                .threshold
                                .value
                                .shouldBeCloseTo(EqualityThreshold.DEFAULT)
                        }
                    }
                }
                "provided by the user should" - {
                    "create a valid constraint if the threshold is valid" {
                        checkAll(
                            Arb.double(min = 0.0, includeNonFiniteEdgeCases = false),   // Valid thresholds
                            arbFunction(),  // Functions to apply to the solution
                            arbFunction()   // Functions to apply to the solution
                        ) { threshold, left, right ->
                            EqualityConstraint.withDefaultThreshold(left, right, threshold)
                                .shouldBeRight()
                                .threshold
                                .value
                                .shouldBeCloseTo(threshold)
                        }
                    }
                    "return a Left with an InvalidThresholdException if the threshold is invalid" {
                        checkAll(
                            Arb.double(max = 0.0)
                                .filter { it < 0.0 || it.isNaN() || it.isInfinite() }, // Invalid thresholds
                            arbFunction(),  // Functions to apply to the solution
                            arbFunction()   // Functions to apply to the solution
                        ) { invalidThreshold, left, right ->
                            EqualityConstraint.withDefaultThreshold(left, right, invalidThreshold)
                                .shouldBeLeft()
                                .shouldHaveMessage(
                                    "Threshold should be non-negative, but was $invalidThreshold"
                                )
                        }
                    }
                }
            }
        }
    }
})

context(validity: Validity.AllValid)
private fun arbEqualityThreshold(): Gen<EqualityThreshold> =
    Arb.double(min = 0.0, includeNonFiniteEdgeCases = false).map { value ->
        EqualityThreshold(value)
            .getOrElse { error("Expected a valid threshold, but got: $it") }
    }

context(validity: Validity.AllInvalid)
private fun arbEqualityThreshold(): Gen<InvalidThresholdException> =
    Arb.double(max = 0.0)
        .filter { it < 0.0 || it.isNaN() || it.isInfinite() }
        .map { value ->
            EqualityThreshold(value)
                .leftOrElse { error("Expected an invalid threshold, but got: $it") }
        }

private fun <A, B> Either<A, B>.leftOrElse(function: (B) -> A): A = when (this) {
    is Either.Left -> value
    is Either.Right -> function(value)
}

private val functions = listOf(
    { solution: Solution<Int> -> solution.first().toDouble() },
    { solution: Solution<Int> -> solution.tail().first().toDouble() },
    { solution: Solution<Int> -> solution.sumOf { it.toDouble() } },
    { solution: Solution<Int> -> solution.map { it.toDouble() }.average() },
)

private fun arbFunction(): Arb<(Solution<Int>) -> Double> = Arb.element(functions)

private sealed interface Validity {
    data object AllValid : Validity
    data object AllInvalid : Validity
    data object Mixed : Validity
}
