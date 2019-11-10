package com.madyanov.ecs

import com.madyanov.ecs.mocks.components.Initiative
import com.madyanov.ecs.mocks.components.Position
import com.madyanov.ecs.mocks.components.Velocity
import com.madyanov.ecs.mocks.systems.MovementSystem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NexusTest {
    private val nexus = Nexus()

    @Test
    fun `test entity creation`() {
        Assertions.assertEquals(0, nexus.makeEntity().key)
        Assertions.assertEquals(1, nexus.makeEntity().key)
    }

    @Test
    fun `test numberOfEntities property`() {
        Assertions.assertEquals(0, nexus.numberOfEntities)

        val entityId1 = nexus.makeEntity()
        val entityId2 = nexus.makeEntity()
        Assertions.assertEquals(2, nexus.numberOfEntities)

        nexus.removeEntity(entityId1)
        Assertions.assertEquals(1, nexus.numberOfEntities)

        nexus.removeEntity(entityId2)
        Assertions.assertEquals(0, nexus.numberOfEntities)

        Assertions.assertEquals(1, nexus.makeEntity().key)
        Assertions.assertEquals(0, nexus.makeEntity().key)
    }

    @Test
    fun `test system synchronization`() {
        val movementSystem = MovementSystem()
        nexus.addSystem(movementSystem)

        nexus.makeEntity(listOf(Position()))
        nexus.makeEntity(listOf(Velocity()))

        val entityId3 = nexus.makeEntity(listOf(Position(), Velocity()))
        val entityId4 = nexus.makeEntity(listOf(Position(), Velocity()))

        Assertions.assertEquals(2, movementSystem.entityIds.size)
        Assertions.assertEquals(listOf(entityId3, entityId4), movementSystem.entityIds.elements)

        nexus.remove(Position::class, entityId3)
        Assertions.assertEquals(1, movementSystem.entityIds.size)
        Assertions.assertEquals(listOf(entityId4), movementSystem.entityIds.elements)

        nexus.removeEntity(entityId4)
        Assertions.assertTrue(movementSystem.entityIds.isEmpty)
    }

    @Test
    fun `test non existing entity removing`() {
        val movementSystem = MovementSystem()
        nexus.addSystem(movementSystem)

        val entityId = nexus.makeEntity(listOf(Position()))
        Assertions.assertFalse(movementSystem.removeMethodCalled)

        nexus.removeEntity(entityId)
        Assertions.assertFalse(movementSystem.removeMethodCalled)
    }

    @Test
    fun `test has method`() {
        val entityId = nexus.makeEntity()
        Assertions.assertFalse(nexus.has(Position::class, entityId))
        Assertions.assertFalse(nexus.has(Velocity::class, entityId))

        nexus.assign(Position(42, 33), entityId)
        Assertions.assertTrue(nexus.has(Position::class, entityId))
        Assertions.assertFalse(nexus.has(Velocity::class, entityId))
    }

    @Test
    fun `test component removing`() {
        val entityId = nexus.makeEntity(listOf(Position(42, 33)))
        Assertions.assertTrue(nexus.has(Position::class, entityId))

        nexus.remove(Position::class, entityId)
        Assertions.assertFalse(nexus.has(Position::class, entityId))
    }

    @Test
    fun `test get method and component updating`() {
        val entityId = nexus.makeEntity()

        val nullPosition = nexus.get(Position::class, entityId)
        Assertions.assertNull(nullPosition)

        nexus.assign(Position(42, 33), entityId)
        val position = nexus.get(Position::class, entityId)
        Assertions.assertEquals(42, position?.x)
        Assertions.assertEquals(33, position?.y)

        position?.x = 777
        val updatedPosition = nexus.get(Position::class, entityId)
        Assertions.assertEquals(777, updatedPosition?.x)
    }

    @Test
    fun `test component replacing`() {
        val entityId = nexus.makeEntity()

        nexus.assign(Position(42, 33), entityId)
        val position1 = nexus.get(Position::class, entityId)
        Assertions.assertEquals(42, position1?.x)
        Assertions.assertEquals(33, position1?.y)

        nexus.assign(Position(43, 34), entityId)
        val position2 = nexus.get(Position::class, entityId)
        Assertions.assertEquals(43, position2?.x)
        Assertions.assertEquals(34, position2?.y)
    }

    @Test
    fun `test numberOfComponents property`() {
        Assertions.assertEquals(0, nexus.numberOfComponents)

        val entityId1 = nexus.makeEntity(listOf(Velocity()))
        Assertions.assertEquals(1, nexus.numberOfComponents)

        val entityId2 = nexus.makeEntity(listOf(Velocity(), Initiative()))
        Assertions.assertEquals(3, nexus.numberOfComponents)

        nexus.removeEntity(entityId1)
        Assertions.assertEquals(2, nexus.numberOfComponents)

        nexus.remove(Velocity::class, entityId2)
        Assertions.assertEquals(1, nexus.numberOfComponents)

        nexus.removeEntity(entityId2)
        Assertions.assertEquals(0, nexus.numberOfComponents)
    }
}
