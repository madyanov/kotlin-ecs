package com.madyanov.ecs

import kotlin.reflect.KClass

class EntityTraitSet(
    private val required: Set<KClass<out Component>>,
    private val excluded: Set<KClass<out Component>> = setOf()
) {
    fun match(components: Set<KClass<out Component>>): Boolean =
        components.containsAll(required) && !excluded.any { components.contains(it) }
}
