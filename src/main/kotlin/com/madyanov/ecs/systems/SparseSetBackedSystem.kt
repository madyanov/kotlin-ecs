package com.madyanov.ecs.systems

import com.madyanov.ecs.*
import java.lang.ref.WeakReference

abstract class SparseSetBackedSystem(nexus: Nexus) : System {

    abstract override val traits: EntityTraitSet

    protected val nexus: Nexus
        get() = weakNexus.get()!!

    protected val entityIds = SparseSet<EntityIdentifier>()

    private val weakNexus = WeakReference(nexus)

    abstract override fun update()

    override fun has(entityId: EntityIdentifier): Boolean = entityIds.contains(entityId)
    override fun add(entityId: EntityIdentifier) = entityIds.insert(entityId).discard()
    override fun remove(entityId: EntityIdentifier) = entityIds.remove(entityId).discard()
}
