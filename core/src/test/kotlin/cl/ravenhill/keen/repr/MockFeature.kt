/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

/**
 * Lightweight test implementation of the [Feature] abstraction using a value class.
 *
 * [MockFeature] is intended for property-based testing and serves as a minimal, lawful implementation of the [Feature]
 * interface. It supports monadic and applicative operations and is typically used to validate functor and monad laws
 * without introducing domain-specific logic.
 *
 * This implementation is a `value class`, allowing it to avoid allocation overhead in most cases while preserving
 * behavior suitable for test verification.
 *
 * @param T The type of value wrapped by the feature.
 * @property x The underlying value stored in this feature.
 */
@JvmInline
internal value class MockFeature<T>(val x: T) : Feature<T, MockFeature<T>> {

    /**
     * Applies the given function to the inner value and returns the resulting feature.
     *
     * This satisfies the monadic bind operation.
     *
     * @param T2 The type of the resulting value.
     * @param F2 The resulting feature type.
     * @param f Function that maps the inner value to a new feature instance.
     * @return The feature produced by applying [f] to [x].
     */
    override fun <T2, F2> flatMap(f: (T) -> F2): F2 where F2 : Feature<T2, F2> = f(x)

    /**
     * Combines this feature with another using the given function.
     *
     * This satisfies the applicative zip operation.
     *
     * @param other Another [MockFeature] with a value to combine.
     * @param combine A function that combines both values into a new value.
     * @return A new [MockFeature] wrapping the result of [combine].
     */
    override fun zipWith(other: MockFeature<T>, combine: (T, T) -> T): MockFeature<T> =
        MockFeature(x = combine(x, other.x))
}

/**
 * Factory for creating [MockFeature] instances for testing purposes.
 *
 * This implementation of [FeatureFactory] provides the canonical constructor function [pure], which lifts a value into
 * the [MockFeature] context.
 * It is used in law-checking test suites to supply values and transformations uniformly.
 *
 * @param T The type of value stored in the feature.
 * @property pure The canonical constructor for creating [MockFeature] instances.
 */
internal class MockFeatureFactory<T> : FeatureFactory<T, MockFeature<T>> {
    override val pure: (T) -> MockFeature<T> = { x: T -> MockFeature(x) }
}
