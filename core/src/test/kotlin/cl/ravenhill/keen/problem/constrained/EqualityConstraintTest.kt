/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.problem.constrained

import arrow.core.Either
import arrow.core.tail
import cl.ravenhill.keen.Solution
import cl.ravenhill.keen.exceptions.InvalidThresholdException
import cl.ravenhill.keen.matchers.shouldBeCloseTo
import cl.ravenhill.keen.util.EqualityThreshold
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.Gen
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class EqualityConstraintTest : FreeSpec({
    "An 'Equality Constraint' when" - {

        "creating an instance in the context of a valid equality threshold should" - {

            with(Validity.AllValid) {

                "bind the threshold to the constraint" {
                    checkAll(arbEqualityThreshold(), arbFunction(), arbFunction()) { rawThreshold, left, right ->
                        with(rawThreshold) {
                            when (rawThreshold) {
                                is Either.Left -> fail("Expected a valid threshold, but got: $rawThreshold")
                                is Either.Right -> {
                                    val threshold = rawThreshold.value
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
            }
        }

        "creating an instance in the context of an invalid equality threshold should" - {

            with(Validity.AllInvalid) {

                "return a Left with an InvalidThresholdException" {

                }
            }
        }
    }
})

context(validity: Validity.AllValid)
private fun arbEqualityThreshold(): Gen<Either<InvalidThresholdException, EqualityThreshold>> =
    Arb.double(min = 0.0, includeNonFiniteEdgeCases = false).map { value ->
        EqualityThreshold(value).also {
            require(it.isRight()) { "Invalid threshold generated: ${it.leftOrNull()}" }
        }
    }

context(validity: Validity.AllInvalid)
private fun arbEqualityThreshold(): Gen<Either<InvalidThresholdException, EqualityThreshold>> =
    Arb.double(max = 0.0).map { value ->
        EqualityThreshold(value).also {
            require(it.isLeft()) { "Expected an invalid threshold, but got: ${it.getOrNull()}" }
        }
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
    data object SomeValid : Validity
}
