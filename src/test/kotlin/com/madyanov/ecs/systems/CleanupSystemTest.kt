package com.madyanov.ecs.systems

import com.madyanov.ecs.Nexus
import com.madyanov.ecs.mocks.components.Position
import com.madyanov.ecs.mocks.components.Velocity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CleanupSystemTest {
    private val nexus = Nexus()
    private val cleanupSystem = CleanupSystem(nexus, Position::class)

    init {
        nexus.addSystem(cleanupSystem)
    }

    @Test
    fun `test component removing`() {
        val entityId1 = nexus.makeEntity(Position())
        val entityId2 = nexus.makeEntity(Position())
        val entityId3 = nexus.makeEntity(Velocity())

        nexus.updateSystems()

        Assertions.assertFalse(nexus.has(Position::class, entityId1))
        Assertions.assertFalse(nexus.has(Position::class, entityId2))
        Assertions.assertTrue(nexus.has(Velocity::class, entityId3))
    }
}
