/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.problem.Objective

/**
 * Represents a candidate solution in an optimization problem.
 *
 * A [Solution] holds a list of values of type [T], which typically represent variables, genes, or decision components
 * depending on the problem domain.
 * Solutions are evaluated using an [Objective], and may be subject to constraints or modified by evolutionary
 * operators.
 *
 * ## Usage:
 * This interface provides indexed access via `solution[i]` and exposes the raw [values] list.
 *
 * @param T The type of the elements contained in the solution.
 *
 * @property values The ordered list of values that define this solution.
 */
interface Solution<T> {

    /** The underlying list of values that represent the solution. */
    val values: List<T>

    /**
     * Returns the value at the specified [index] in the solution.
     *
     * @param index The position of the value to retrieve.
     * @return The value at the given index.
     */
    operator fun get(index: Int): T = values[index]
}
