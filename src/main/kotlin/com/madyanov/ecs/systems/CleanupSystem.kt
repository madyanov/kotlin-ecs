package com.madyanov.ecs.systems

import com.madyanov.ecs.Component
import com.madyanov.ecs.EntityTraitSet
import com.madyanov.ecs.Nexus
import kotlin.reflect.KClass

class CleanupSystem(
    nexus: Nexus,
    private val klass: KClass<out Component>
) : SparseSetBackedSystem(nexus) {

    override val traits: EntityTraitSet
        get() = EntityTraitSet(setOf(klass))

    override fun update() = entityIds.toList().forEach { nexus?.remove(klass, it) }
}
