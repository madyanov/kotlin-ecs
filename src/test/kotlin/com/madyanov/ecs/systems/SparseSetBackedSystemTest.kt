package com.madyanov.ecs.systems

import com.madyanov.ecs.Nexus
import com.madyanov.ecs.mocks.components.Position
import com.madyanov.ecs.mocks.components.Velocity
import com.madyanov.ecs.mocks.systems.SparseSetBackedMovementSystem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SparseSetBackedSystemTest {
    private val nexus = Nexus()
    private val movementSystem = SparseSetBackedMovementSystem(nexus)

    init {
        nexus.addSystem(movementSystem)
    }

    @Test
    fun `test system synchronization`() {
        nexus.makeEntity(Position())
        nexus.makeEntity(Velocity())

        val entityId3 = nexus.makeEntity(Position(), Velocity())
        val entityId4 = nexus.makeEntity(Position(), Velocity())

        Assertions.assertEquals(2, movementSystem.publicEntityIds.size)
        Assertions.assertEquals(listOf(entityId3, entityId4), movementSystem.publicEntityIds.elements)

        nexus.remove(Position::class, entityId3)
        Assertions.assertEquals(1, movementSystem.publicEntityIds.size)
        Assertions.assertEquals(listOf(entityId4), movementSystem.publicEntityIds.elements)

        nexus.removeEntity(entityId4)
        Assertions.assertTrue(movementSystem.publicEntityIds.isEmpty)
    }
}
