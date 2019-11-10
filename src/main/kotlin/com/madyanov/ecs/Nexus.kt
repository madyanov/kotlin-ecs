package com.madyanov.ecs

import kotlin.reflect.KClass

class Nexus(systems: List<System> = listOf()) {

    val numberOfEntities: Int
        get() = entityIds.size

    val numberOfComponents: Int
        get() = componentsByComponentClasses.toList().sumBy { it.second.size }

    private val entityIdsPool = EntityIdentifiersPool()
    private val entityIds = SparseSet<EntityIdentifier>()
    private val componentsByComponentClasses = mutableMapOf<KClass<out Component>, SparseSet<Component>>()
    private val componentClassesByEntityId = mutableMapOf<EntityIdentifier, MutableSet<KClass<out Component>>>()
    private val systems = mutableListOf<Pair<System, EntityTraitSet>>()

    init {
        addSystems(systems)
    }

    fun addSystems(systems: List<System>) {
        this.systems += systems.map { Pair(it, it.traits) }
    }

    fun addSystem(system: System) = addSystems(listOf(system))

    fun updateSystems() = systems.forEach { it.first.update() }

    fun makeEntity(components: List<Component> = listOf()): EntityIdentifier {
        val entityId = entityIdsPool.take()
        components.forEach { assign(it, entityId) }
        entityIds.insert(entityId)
        return entityId
    }

    fun removeEntity(entityId: EntityIdentifier) {
        assert(entityIds.contains(entityId))

        entityIdsPool.free(entityId)
        entityIds.remove(entityId)

        componentClassesByEntityId[entityId]?.forEach {
            assert(componentsByComponentClasses[it]?.contains(entityId.key) ?: false)
            componentsByComponentClasses[it]?.remove(entityId.key)
        }

        componentClassesByEntityId.remove(entityId)

        updateSystemsMembership(entityId)
    }

    fun assign(component: Component, entityId: EntityIdentifier) {
        if (componentsByComponentClasses[component::class] == null) {
            componentsByComponentClasses[component::class] = SparseSet()
        }

        if (componentClassesByEntityId[entityId] == null) {
            componentClassesByEntityId[entityId] = HashSet()
        }

        componentsByComponentClasses[component::class]?.insert(component, entityId.key)
        componentClassesByEntityId[entityId]?.add(component::class)

        updateSystemsMembership(entityId)
    }

    fun remove(component: KClass<out Component>, entityId: EntityIdentifier) {
        assert(componentsByComponentClasses[component]?.contains(entityId.key) ?: false)
        assert(componentClassesByEntityId[entityId]?.contains(component) ?: false)

        componentsByComponentClasses[component]?.remove(entityId.key)
        componentClassesByEntityId[entityId]?.remove(component)

        updateSystemsMembership(entityId)
    }

    fun has(component: KClass<out Component>, entityId: EntityIdentifier): Boolean =
        componentsByComponentClasses[component]?.contains(entityId.key) ?: false

    @Suppress("UNCHECKED_CAST")
    fun <C : Component> get(component: KClass<out C>, entityId: EntityIdentifier): C? =
        componentsByComponentClasses[component]?.get(entityId.key) as? C

    private fun updateSystemsMembership(entityId: EntityIdentifier) {
        val components = componentClassesByEntityId[entityId] ?: mutableSetOf()

        systems.forEach {
            val (system, traits) = it
            val contains = system.has(entityId)
            val matches = traits.match(components)

            when {
                !contains && matches -> system.add(entityId)
                contains && !matches -> system.remove(entityId)
            }
        }
    }
}
