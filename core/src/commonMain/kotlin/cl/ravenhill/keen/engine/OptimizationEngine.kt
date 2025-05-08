/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.engine

import cl.ravenhill.keen.state.State

interface OptimizationEngine {
    fun <S> optimize(initialState: S): S where S : State<S>
}
