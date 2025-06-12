/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.problem.constrained

import arrow.core.Either
import arrow.core.raise.either
import cl.ravenhill.keen.Solution
import cl.ravenhill.keen.exceptions.InvalidThresholdException
import cl.ravenhill.keen.util.EqualityThreshold
import kotlin.math.abs

interface EqualityConstraint<T> : Constraint<T> {

    val threshold: EqualityThreshold

    override fun invoke(solution: Solution<T>) =
        abs(left(solution) - right(solution)) <= threshold

    private operator fun Double.compareTo(other: EqualityThreshold) = when {
        this < other.value -> -1
        this > other.value -> 1
        else -> 0
    }

    companion object {
        context(threshold: Either<InvalidThresholdException, EqualityThreshold>)
        operator fun <T> invoke(
            left: (Solution<T>) -> Double,
            right: (Solution<T>) -> Double
        ): Either<InvalidThresholdException, EqualityConstraint<T>> = either {
            val boundThreshold = threshold.bind()
            object : EqualityConstraint<T> {
                override val threshold: EqualityThreshold = boundThreshold
                override val left: (Solution<T>) -> Double = left
                override val right: (Solution<T>) -> Double = right
            }
        }

        fun <T> withDefaultThreshold(
            lhs: (Solution<T>) -> Double,
            rhs: (Solution<T>) -> Double,
            threshold: Double = EqualityThreshold.Companion.DEFAULT
        ): Either<InvalidThresholdException, EqualityConstraint<T>> =
            with(EqualityThreshold(threshold)) {
                invoke(lhs, rhs)
            }
    }
}
