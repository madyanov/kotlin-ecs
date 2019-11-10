package com.madyanov.ecs

internal class EntityIdentifiersPool {

    val numberOfEntities: Int
        get() = id.key - freeIds.size

    private var id = EntityIdentifier()
    private var freeIds = mutableListOf<EntityIdentifier>()

    fun take(): EntityIdentifier {
        freeIds.lastOrNull()?.let {
            freeIds.removeAt(freeIds.size - 1)
            return it
        }

        try {
            return id
        } finally {
            id = EntityIdentifier(id.key + 1)
        }
    }

    fun free(id: EntityIdentifier) = freeIds.add(id)

    fun flush() {
        freeIds.clear()
        id = EntityIdentifier()
    }
}
