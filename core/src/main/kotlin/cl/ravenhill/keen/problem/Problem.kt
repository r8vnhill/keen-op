/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.problem

import arrow.core.NonEmptyCollection
import arrow.core.nonEmptyListOf
import cl.ravenhill.keen.util.EqualityThreshold
import cl.ravenhill.keen.util.InequalityType

interface Problem {
    val objectives: NonEmptyCollection<Objective<*>>
    val constraints: Collection<Constraint<*>>

    companion object {
        operator fun invoke(
            objectives: NonEmptyCollection<Objective<*>>,
            constraints: Collection<Constraint<*>> = listOf()
        ) =
            object : Problem {
                override val objectives = objectives
                override val constraints = constraints
            }

        operator fun invoke(
            objective: Objective<*>,
            vararg objectives: Objective<*>,
            constraints: Collection<Constraint<*>> = listOf()
        ) = Problem(nonEmptyListOf(objective, *objectives), constraints)
    }
}

val Problem.asString: String
    get() = "Problem(objectives=$objectives, constraints=$constraints)"

fun main() {
    with(EqualityThreshold(1e-9)) {
        /* **Example problem: Maximize the function**
           f(x1, x2, x3, x4) = x1 * x4 * (x1 + x2 + x3) + x3
           subject to:
           1. x1^2 + x2^2 + x3^2 + x4^2 = 40
           2. x1 * x2 * x3 * x4 <= 25
         */
        Problem(
            Objective<Int> { (it[0] * it[3] * (it[0] + it[1] + it[2]) + it[2]).toDouble() },
//            constraints = listOf(
//                EqualityConstraint<Int>(
//                    left = { (it[0] * it[0] + it[1] * it[1] + it[2] * it[2] + it[3] * it[3]).toDouble() },
//                    right = { 40.0 },
//                ),
//                InequalityConstraint(
//                    left = { (it[0] * it[1] * it[2] * it[3]).toDouble() },
//                    right = { 25.0 },
//                    operator = InequalityType.LESS_THAN_OR_EQUAL,
//                )
//            )
        ).asString.also(::println)
    }
}
