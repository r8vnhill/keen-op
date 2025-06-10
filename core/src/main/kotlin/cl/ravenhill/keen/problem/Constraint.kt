/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.problem

import arrow.core.Either
import arrow.core.raise.either
import cl.ravenhill.keen.Solution
import cl.ravenhill.keen.exceptions.InvalidThresholdException
import cl.ravenhill.keen.util.EqualityThreshold
import cl.ravenhill.keen.util.InequalityType
import kotlin.math.abs

/**
 * Represents a generic constraint on a [Solution] that evaluates to a boolean.
 *
 * This sealed interface models both equality and inequality constraints as functions from [Solution] to [Boolean].
 * Constraints are evaluated by comparing the left-hand side ([left]) and right-hand side ([right]) expressions.
 *
 * ## Usage:
 * This interface is not meant to be used directly.
 * Prefer using [EqualityConstraint] or [InequalityConstraint].
 *
 * @property left The left-hand side expression of the constraint, evaluated per [Solution].
 * @property right The right-hand side expression of the constraint, evaluated per [Solution].
 * @param T The type of the solution being constrained.
 */
sealed interface Constraint<T> : (Solution<T>) -> Boolean {
    val left: (Solution<T>) -> Double
    val right: (Solution<T>) -> Double
}

interface EqualityConstraint<T> : Constraint<T> {

    val threshold: EqualityThreshold

    override fun invoke(solution: Solution<T>) =
        abs(left(solution) - right(solution)) <= threshold

    operator fun Double.compareTo(other: EqualityThreshold) = when {
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
            threshold: Double = EqualityThreshold.DEFAULT
        ): Either<InvalidThresholdException, EqualityConstraint<T>> =
            with(EqualityThreshold(threshold)) {
                invoke(lhs, rhs)
            }
    }
}

/**
 * Represents an inequality constraint on a [Solution] using a directional [InequalityType].
 *
 * This constraint is satisfied when the comparison between [left] and [right] matches the given [type].
 * For example, a `LESS_THAN` constraint requires `lhs(solution) < rhs(solution)`.
 *
 * @property type The inequality type used to compare the expressions.
 */
interface InequalityConstraint<T> : Constraint<T> {

    val type: InequalityType

    /**
     * Evaluates the constraint using the configured inequality [type].
     *
     * @param solution The solution to evaluate.
     * @return `true` if the constraint is satisfied; `false` otherwise.
     */
    override fun invoke(solution: Solution<T>) = when (type) {
        InequalityType.LESS_THAN -> left(solution) < right(solution)
        InequalityType.GREATER_THAN -> left(solution) > right(solution)
        InequalityType.LESS_THAN_OR_EQUAL -> left(solution) <= right(solution)
        InequalityType.GREATER_THAN_OR_EQUAL -> left(solution) >= right(solution)
    }

    companion object {
        /**
         * Creates an [InequalityConstraint] with the specified [left], [right], and comparison [operator].
         *
         * @param left The left-hand side expression.
         * @param right The right-hand side expression.
         * @param operator The type of inequality (defaults to [InequalityType.LESS_THAN]).
         * @param T The type of the solution being constrained.
         * @return An [InequalityConstraint] instance.
         */
        operator fun <T> invoke(
            left: (Solution<T>) -> Double,
            right: (Solution<T>) -> Double,
            operator: InequalityType = InequalityType.LESS_THAN,
        ): InequalityConstraint<T> = object : InequalityConstraint<T> {
            override val type: InequalityType = operator
            override val left: (Solution<T>) -> Double = left
            override val right: (Solution<T>) -> Double = right
        }
    }
}
