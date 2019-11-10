package com.madyanov.ecs.systems

import com.madyanov.ecs.Nexus
import com.madyanov.ecs.mocks.components.Position
import com.madyanov.ecs.mocks.components.Velocity
import com.madyanov.ecs.mocks.systems.SparseSetBackedMovementSystem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SparseSetBackedSystemTest {
    private val nexus = Nexus()
    private val movementSystem: SparseSetBackedMovementSystem

    init {
        movementSystem = SparseSetBackedMovementSystem(nexus)
        nexus.addSystem(movementSystem)
    }

    @Test
    fun `test system synchronization`() {
        nexus.makeEntity(listOf(Position()))
        nexus.makeEntity(listOf(Velocity()))

        val entityId3 = nexus.makeEntity(listOf(Position(), Velocity()))
        val entityId4 = nexus.makeEntity(listOf(Position(), Velocity()))

        Assertions.assertEquals(2, movementSystem.publicEntityIds.size)
        Assertions.assertEquals(listOf(entityId3, entityId4), movementSystem.publicEntityIds.elements)

        nexus.remove(Position::class, entityId3)
        Assertions.assertEquals(1, movementSystem.publicEntityIds.size)
        Assertions.assertEquals(listOf(entityId4), movementSystem.publicEntityIds.elements)

        nexus.removeEntity(entityId4)
        Assertions.assertTrue(movementSystem.publicEntityIds.isEmpty)
    }
}
