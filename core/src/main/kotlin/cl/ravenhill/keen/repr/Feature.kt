/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

/**
 * Core abstraction representing a computational feature over values of type [T].
 *
 * This interface unifies the **Functor**, **Applicative**, and **Monad** patterns:
 *
 * 1. **Functor** – Ability to transform the wrapped value without changing its context:
 *    - Provided by [map], which applies a function `(T) -> T` and returns a new feature.
 * 2. **Applicative** – Ability to combine two feature contexts:
 *    - Provided by [zipWith], which merges two features by applying a binary function `(T, T) -> T`.
 * 3. **Monad** – Ability to sequence computations that produce new feature contexts:
 *    - Provided by [flatMap], which applies a function `(T) -> F2` returning a new feature context.
 *
 * ## Usage:
 *
 * ### Example 1: Functor mapping
 * ```kotlin
 * // Given a feature wrapping an Int, increment its value
 * val original: Feature<Int, *> = MyFeature(5)
 * val incremented: Feature<Int, *> = original.map { it + 1 }
 * ```
 *
 * ### Example 2: Applicative zipping
 * ```kotlin
 * // Combine two features by summing their values
 * val f1: Feature<Int, *> = MyFeature(2)
 * val f2: Feature<Int, *> = MyFeature(3)
 * val summed: Feature<Int, *> = f1.zipWith(f2) { a, b -> a + b }  // result wraps 5
 * ```
 *
 * ### Example 3: Monad flat-mapping
 * ```kotlin
 * // Sequence computations that produce new features
 * val f: Feature<Int, *> = MyFeature(4)
 * val chained: Feature<Int, *> = f.flatMap { value ->
 *     MyFeature(value * 2)  // next feature depends on previous value
 * }
 * ```
 *
 * @param T  The type of the value carried by this feature.
 * @param F  The concrete feature type that implements this interface (i.e., `F : Feature<T, F>`).
 */
interface Feature<out T, F> where F : Feature<@UnsafeVariance T, F> {

    /**
     * Transforms the contained value using [f], preserving the feature context.
     *
     * This implements the **Functor** pattern.
     *
     * @param f Function to apply to the current value.
     * @return A new instance of [F] containing the transformed value.
     */
    context(factory: FeatureFactory<@UnsafeVariance T, F>)
    fun map(f: (T) -> @UnsafeVariance T): F = flatMap { factory.pure(f(it)) }

    /**
     * Chains a computation that returns a new feature context.
     *
     * This implements the **Monad** pattern.
     *
     * @param T2 The type of the value in the resulting feature.
     * @param F2 The concrete feature type of the resulting feature (`F2 : Feature<T2, F2>`).
     * @param f  Function that maps the current value to a new feature.
     * @return The feature produced by applying [f].
     */
    fun <T2, F2> flatMap(f: (T) -> F2): F2 where F2 : Feature<T2, F2>

    /**
     * Combines this feature with [other] by applying [combine] to their values.
     *
     * This implements the **Applicative** pattern for merging contexts.
     *
     * @param other   Another feature of the same type [F].
     * @param combine Binary function to merge both feature values.
     * @return A new instance of [F] containing the combined result.
     */
    fun zipWith(other: F, combine: (T, T) -> @UnsafeVariance T): F
}
