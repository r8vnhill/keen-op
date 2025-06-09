/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class FeatureFactoryTest : FreeSpec({
    "A generic Feature Factory when" - {

        fun <T> featureFactory(): FeatureFactory<T, MockFeature<T>> = MockFeatureFactory()

        "creating a Mock Feature of" - {

            fun <T> of(t: T): MockFeature<T> = MockFeature(t)

            suspend fun <T> checkConstruction(arb: Arb<T>) = checkAll(arb) { value ->
                val factory = featureFactory<T>()
                factory.pure(value) shouldBe of(value)
                factory.of(value) shouldBe of(value)
                factory.just(value) shouldBe of(value)
            }

            mapOf(
                "an Int type should" to Arb.int(),
                "a String type should" to Arb.string(),
                "a List of Ints should" to Arb.list(Arb.int()),
                "a Map of Ints to Strings should" to Arb.map(Arb.int(), Arb.string())
            ).forEach { (description, arb) ->
                description - {
                    "construct a MockFeature" {
                        checkConstruction(arb)
                    }
                }
            }
        }

        "lifting a function" - {

            fun <T> lift(f: (T) -> T): (MockFeature<T>) -> MockFeature<T> = featureFactory<T>().lift(f)

            "should apply the function to the value inside the MockFeature" {
                checkAll(Arb.int()) { value ->
                    val f: (Int) -> Int = { it + 1 }
                    val lifted = lift(f)
                    lifted(MockFeature(value)).x shouldBe f(value)
                }
            }
        }
    }
})
