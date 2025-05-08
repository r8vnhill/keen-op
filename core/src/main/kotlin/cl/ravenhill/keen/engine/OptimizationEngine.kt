/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.engine

import cl.ravenhill.keen.repr.Feature


interface OptimizationEngine<F>  where F : Feature<*, F> {

    val objectiveFunction: (F) -> Double

    fun optimize(initialState: F): F
}
