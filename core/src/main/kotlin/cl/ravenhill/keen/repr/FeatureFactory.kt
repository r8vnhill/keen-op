/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

/**
 * Factory interface for constructing and lifting values into a [Feature] context.
 *
 * This interface provides canonical ways to create and manipulate instances of a [Feature], enabling functional
 * composition and abstraction over feature constructors.
 *
 * It supports key functional idioms:
 *
 * - **Pure**: Introduces a raw value into the [Feature] context.
 * - **Aliases**: [of] and [just] offer alternative, expressive ways to lift a value.
 * - **Lift**: Transforms a function `(T) -> T` into a function `(F) -> F`, i.e., lifting a pure function to operate
 *   over the feature context (Functor behavior).
 *
 * This abstraction is particularly useful in testing, metaprogramming, or generic programming where
 * values need to be lifted, transformed, or composed independently of the specific [Feature] implementation.
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature being produced, which must implement [Feature].
 *
 * @property pure The canonical constructor that lifts a raw value into the [Feature] context.
 */
interface FeatureFactory<T, F> where F : Feature<T, F> {

    /** Canonical constructor function that lifts a value into the [Feature] context. */
    val pure: (value: T) -> F

    /**
     * Alias for [pure], provided for semantic clarity or preference.
     *
     * @param value The value to lift.
     * @return A [Feature] instance wrapping [value].
     */
    fun of(value: T): F = pure(value)

    /**
     * Alias for [pure], often used to align with terminology from functional programming.
     *
     * @param value The value to lift.
     * @return A [Feature] instance wrapping [value].
     */
    fun just(value: T): F = pure(value)

    /**
     * Lifts a function from `(T) -> T` into `(F) -> F`, allowing it to operate over [Feature] instances.
     *
     * This embodies the **Functor** pattern.
     *
     * @param f A transformation on raw values.
     * @return A function that applies [f] to the contents of any [Feature] instance.
     */
    fun lift(f: (T) -> T): (F) -> F = { it.map(f) }
}
