package com.madyanov.ecs

interface System {
    val traits: EntityTraitSet

    fun update()

    fun has(entityId: EntityIdentifier): Boolean
    fun add(entityId: EntityIdentifier)
    fun remove(entityId: EntityIdentifier)
}
