package com.madyanov.ecs

import com.madyanov.ecs.mocks.components.Initiative
import com.madyanov.ecs.mocks.components.Position
import com.madyanov.ecs.mocks.components.Velocity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EntityTraitSetTest {

    @Test
    fun `test matching with one required component`() {
        val traits = EntityTraitSet(setOf(Position::class))

        Assertions.assertFalse(traits.match(setOf(Velocity::class, Initiative::class)))
        Assertions.assertTrue(traits.match(setOf(Velocity::class, Initiative::class, Position::class)))
    }

    @Test
    fun `test matching with two required components`() {
        val traits = EntityTraitSet(setOf(Position::class, Velocity::class))

        Assertions.assertFalse(traits.match(setOf(Velocity::class, Initiative::class)))
        Assertions.assertFalse(traits.match(setOf(Initiative::class, Position::class)))
        Assertions.assertTrue(traits.match(setOf(Velocity::class, Initiative::class, Position::class)))
        Assertions.assertTrue(traits.match(setOf(Velocity::class, Position::class)))
    }

    @Test
    fun `test matching with one excluded component`() {
        val traits = EntityTraitSet(setOf(Position::class), setOf(Initiative::class))

        Assertions.assertFalse(traits.match(setOf(Velocity::class, Initiative::class)))
        Assertions.assertFalse(traits.match(setOf(Velocity::class, Initiative::class, Position::class)))
        Assertions.assertTrue(traits.match(setOf(Velocity::class, Position::class)))
    }

    @Test
    fun `test matching with two excluded components`() {
        val traits = EntityTraitSet(setOf(Position::class), setOf(Initiative::class, Velocity::class))

        Assertions.assertFalse(traits.match(setOf(Velocity::class, Initiative::class)))
        Assertions.assertFalse(traits.match(setOf(Velocity::class, Initiative::class, Position::class)))
        Assertions.assertFalse(traits.match(setOf(Velocity::class, Position::class)))
        Assertions.assertTrue(traits.match(setOf(Position::class)))
    }
}
