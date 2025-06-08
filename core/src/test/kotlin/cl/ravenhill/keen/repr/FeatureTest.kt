/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import io.kotest.assertions.withClue
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

/**
 * ✅ **Feature Laws Test Suite**
 *
 * This suite validates that any implementation of the [Feature] abstraction adheres to the standard **Functor** and
 * **Monad** laws.
 * These laws are critical for ensuring composability, correctness, and predictability in functional-style APIs.
 *
 * ### 🔍 Purpose
 *
 * Use this test as a **template** when implementing your own [Feature] types to verify that your type behaves
 * consistently with functional programming principles.
 *
 * > ⚠️ This suite uses a mock implementation called [`MockFeature`] for demonstration purposes.
 * > It is not intended for production use, but rather to illustrate the expected behavior of compliant [Feature] types.
 *
 * ---
 *
 * ## 📐 Laws Verified
 *
 * ### 🔷 Functor Laws
 * 1. **Identity**:
 *    ```kotlin
 *    feature.map { it } == feature
 *    ```
 * 2. **Composition**:
 *    ```kotlin
 *    feature.map(f).map(g) == feature.map { g(f(it)) }
 *    ```
 *
 * ### 🔶 Monad Laws
 * 1. **Left Identity**:
 *    ```kotlin
 *    unit(a).flatMap(f) == f(a)
 *    ```
 * 2. **Right Identity**:
 *    ```kotlin
 *    m.flatMap(unit) == m
 *    ```
 * 3. **Associativity**:
 *    ```kotlin
 *    m.flatMap(f).flatMap(g) == m.flatMap { f(it).flatMap(g) }
 *    ```
 *
 * ### 🔁 Additional Operations
 * - **zipWith**: While not part of Functor/Monad laws, this suite also verifies expected behavior
 *   such as commutativity for addition, and structural consistency.
 *
 * ---
 *
 * ## 💡 Tips for Library Authors
 * - Use property-based testing (as shown) to validate new [Feature] implementations.
 * - Extend this suite with laws for additional operations like `filter`, `fold`, or `combine`.
 * - Consider making your [Feature] a lawful `Functor` and `Monad` if you want it to integrate with functional
 *   frameworks.
 *
 * ## 📚 Further Reading
 * - [Your easy guide to Monads, Applicatives, & Functors](https://medium.com/@lettier/your-easy-guide-to-monads-applicatives-functors-862048d61610)
 * - [Functor, Monad, Applicative (Haskell Wiki)](https://wiki.haskell.org/Functor-Applicative-Monad-Proposal)
 */
@DisplayName("Feature Laws")
class FeatureTest : FreeSpec({

    "A Feature" - {
        "functor laws" - {
            "identity: mapping with identity should return the same feature" {
                checkAll(arbFeature()) { feature ->
                    val identity = { x: Int -> x }
                    object : FeatureFactory<Int, MockFeature> {
                        override val pure = ::MockFeature
                    }.run {
                        feature.map(identity) shouldBe feature
                    }
                }
            }

            "composition: mapping f then g == mapping (g ∘ f)" {
                checkAll(arbFeature(), arbComposedFunction, arbComposedFunction) { feature, f, g ->
                    val left = feature.map(f.fn).map(g.fn)
                    val right = feature.map { g.fn(f.fn(it)) }
                    withClue("${f.name} ∘ ${g.name}") {
                        left shouldBe right
                    }
                }
            }
        }

        "monad laws" - {
            // Identity function lifted into Feature context
            val unit: (Int) -> MockFeature = { MockFeature(it) }

            "left identity: flatMap(unit) == unit" {
                checkAll(Arb.int()) { a ->
                    unit(a).flatMap(unit) shouldBe unit(a)
                }
            }

            "right identity: flatMap(unit) == identity" {
                checkAll(arbFeature()) { m ->
                    m.flatMap(unit) shouldBe m
                }
            }

            "associativity: (m.flatMap(f)).flatMap(g) == m.flatMap { f(it).flatMap(g) }" {
                checkAll(arbFeature(), arbIntToFeature(), arbIntToFeature()) { m, f, g ->
                    val left = m.flatMap(f).flatMap(g)
                    val right = m.flatMap { f(it).flatMap(g) }
                    left shouldBe right
                }
            }
        }

        "zipWith" - {
            "should combine two features with the given function" {
                checkAll(arbFeature(), arbFeature(), arbComposedFunction) { a, b, f ->
                    val zipped = a.zipWith(b) { x, y -> f.fn(x + y) }
                    zipped shouldBe MockFeature(f.fn(a.x + b.x))
                }
            }

            "should be commutative for addition" {
                checkAll(arbFeature(), arbFeature()) { a, b ->
                    val add = { x: Int, y: Int -> x + y }
                    a.zipWith(b, add) shouldBe b.zipWith(a, add)
                }
            }
        }
    }
})

/**
 * Generates arbitrary [MockFeature] instances for property-based testing.
 *
 * Uses [Arb.Companion.int] to produce random integers and maps them to [MockFeature] values.
 * This generator is used to verify functor and monad laws over the [Feature] abstraction.
 *
 * @return An [Arb] that produces instances of [MockFeature].
 */
private fun arbFeature(): Arb<MockFeature> =
    Arb.int().map(::MockFeature)

/**
 * Mock implementation of the [Feature] abstraction used for property-based testing.
 *
 * This class is used solely for verifying functor and monad laws in the test suite.
 * It provides minimal but lawful implementations of [map], [flatMap], and [zipWith], enabling isolated validation of
 * the laws without relying on domain-specific logic.
 *
 * @property x The integer value held by this mock feature.
 */
private data class MockFeature(val x: Int) : Feature<Int, MockFeature> {

    /**
     * Applies the given function to the internal value and returns the resulting feature.
     *
     * @param f The function to apply, producing a new feature instance.
     * @return The result of applying [f] to the current value.
     */
    override fun <T2, F2> flatMap(f: (Int) -> F2): F2 where F2 : Feature<T2, F2> = f(x)

    /**
     * Combines this feature with another [MockFeature] using the provided function.
     *
     * @param other Another [MockFeature] to combine with.
     * @param combine The binary function used to merge the two internal values.
     * @return A new instance with the result of combining both values.
     */
    override fun zipWith(other: MockFeature, combine: (Int, Int) -> Int): MockFeature =
        copy(x = combine(x, other.x))
}

/**
 * Associates a human-readable name with a function from [Int] to [Int].
 *
 * This class is used in property-based tests to improve the readability of test clues and error messages when checking
 * function composition laws (e.g., in functor tests).
 *
 * @property name A descriptive label for the function (e.g., "double", "negate").
 * @property fn The function to apply to an integer value.
 */
private data class NamedFunction(
    val name: String,
    val fn: (Int) -> Int
)

/**
 * A predefined list of named integer functions used in property-based testing.
 *
 * These functions are combined and composed to verify functor and monad laws, especially in tests involving function
 * composition.
 * Each function is wrapped in a [NamedFunction] to allow clear labeling in test output and failure clues.
 *
 * Includes a mix of arithmetic, bitwise, and transformation operations.
 */
private val namedFunctions = listOf(
    NamedFunction("add 1") { it + 1 },
    NamedFunction("subtract 1") { it - 1 },
    NamedFunction("double") { it * 2 },
    NamedFunction("half") { it / 2 },
    NamedFunction("square") { it * it },
    NamedFunction("negate") { -it },
    NamedFunction("mod 10") { it % 10 },
    NamedFunction("and 0xFF") { it and 0xFF },
    NamedFunction("or 0x0F") { it or 0x0F },
    NamedFunction("xor 0x0F") { it xor 0x0F }
)

/**
 * Arbitrary generator of composed [NamedFunction] instances for use in property-based testing.
 *
 * This generator produces a function by randomly selecting 1 to 3 functions from [namedFunctions], composing them from
 * left to right (f ∘ g ∘ h), and labeling the result with a human-readable name.
 *
 * The resulting [NamedFunction] can be used to verify function composition laws, such as those required by functor
 * behavior.
 *
 * Example generated name: `"double ∘ add 1 ∘ square"`
 *
 * @return An [Arb] that generates composed [NamedFunction]s with informative names.
 */
private val arbComposedFunction: Arb<NamedFunction> =
    Arb.list(Arb.element(namedFunctions), 1..3).map { fs ->
        val name = fs.joinToString(" ∘ ") { it.name }
        val composedFn: (Int) -> Int = { x -> fs.fold(x) { acc, f -> f.fn(acc) } }
        NamedFunction(name, composedFn)
    }

/**
 * Arbitrary generator of functions from [Int] to [MockFeature], used in monad law testing.
 *
 * This generator maps each randomly composed [NamedFunction] to a function of type `(Int) -> MockFeature` by composing
 * it with the constructor of [MockFeature].
 * The result is a function that first transforms an integer input, then wraps the result in a [MockFeature] instance.
 *
 * These functions are used in property-based tests to verify monadic laws, such as associativity.
 *
 * @return An [Arb] that generates functions from [Int] to [MockFeature].
 */
private fun arbIntToFeature(): Arb<(Int) -> MockFeature> =
    arbComposedFunction.map { f -> f.fn andThen ::MockFeature }

/**
 * Composes two functions into a single function, applying them in left-to-right order.
 *
 * This is an infix version of function composition: `f andThen g` returns a new function that applies `f`, then `g` to
 * the result.
 *
 * Equivalent to `g(f(a))`.
 * Useful for building readable, declarative pipelines.
 *
 * ## Example:
 * ```kotlin
 * val add1 = { x: Int -> x + 1 }
 * val double = { x: Int -> x * 2 }
 * val composed = add1 andThen double
 * composed(3) // returns 8
 * ```
 *
 * @receiver The first function to apply.
 * @param g The function to apply to the result of the receiver.
 * @return A composed function that applies both functions in sequence.
 */
private infix fun <A, B, C> ((A) -> B).andThen(g: (B) -> C): (A) -> C = { a -> g(this(a)) }
