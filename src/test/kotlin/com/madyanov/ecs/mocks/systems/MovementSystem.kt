package com.madyanov.ecs.mocks.systems

import com.madyanov.ecs.*
import com.madyanov.ecs.mocks.components.Position
import com.madyanov.ecs.mocks.components.Velocity

class MovementSystem : System {
    var removeMethodCalled = false

    override val traits: EntityTraitSet
        get() = EntityTraitSet(setOf(Position::class, Velocity::class))

    val entityIds = SparseSet<EntityIdentifier>()

    override fun update() { }

    override fun has(entityId: EntityIdentifier): Boolean = entityIds.contains(entityId.key)
    override fun add(entityId: EntityIdentifier) = entityIds.insert(entityId, entityId.key).discard()

    override fun remove(entityId: EntityIdentifier) {
        entityIds.remove(entityId.key)
        removeMethodCalled = true
    }
}
