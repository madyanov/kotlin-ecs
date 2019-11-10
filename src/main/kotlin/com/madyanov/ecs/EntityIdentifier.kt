package com.madyanov.ecs

inline class EntityIdentifier(val key: Int = 0) : Identifiable<Int> {
    override val id: Int
        get() = key
}
