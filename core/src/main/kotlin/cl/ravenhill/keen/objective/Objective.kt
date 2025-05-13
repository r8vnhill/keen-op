/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.objective

import cl.ravenhill.keen.repr.Feature

interface Objective<F> where F : Feature<*, F>
