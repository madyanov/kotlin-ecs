package com.madyanov.ecs.mocks.systems

import com.madyanov.ecs.EntityIdentifier
import com.madyanov.ecs.EntityTraitSet
import com.madyanov.ecs.Nexus
import com.madyanov.ecs.SparseSet
import com.madyanov.ecs.mocks.components.Position
import com.madyanov.ecs.mocks.components.Velocity
import com.madyanov.ecs.systems.SparseSetBackedSystem

class SparseSetBackedMovementSystem(nexus: Nexus) : SparseSetBackedSystem(nexus) {

    val publicEntityIds: SparseSet<EntityIdentifier>
        get() = entityIds

    override val traits: EntityTraitSet
        get() = EntityTraitSet(setOf(Position::class, Velocity::class))

    override fun update() {}
}
