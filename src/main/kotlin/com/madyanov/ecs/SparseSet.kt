package com.madyanov.ecs

import java.util.*

class SparseSet<Element> : Iterable<Element> {

    val size: Int
        get() = elements.size

    val isEmpty: Boolean
        get() = elements.isEmpty()

    val elements: List<Element> = mutableListOf()
    val keys: List<Int> = mutableListOf()

    private var indices = Array<Int?>(0) { null }

    override fun iterator(): Iterator<Element> = elements.iterator()

    fun contains(key: Int): Boolean = search(key) != null

    fun search(key: Int): Int? {
        if (key >= indices.size) return null
        val index = indices[key] ?: return null
        if (index >= size) return null
        if (keys[index] != key) return null

        return index
    }

    fun insert(element: Element, key: Int): Boolean {
        elements as MutableList
        keys as MutableList

        search(key)?.let { index ->
            elements[index] = element
            keys[index] = key
            return false
        }

        if (key >= indices.size) {
            indices = indices.copyOf(key * 2 + 1)
        }

        elements += element
        keys += key
        indices[key] = elements.size - 1

        return true
    }

    fun get(key: Int): Element? {
        val index = search(key) ?: return null
        return elements[index]
    }

    fun remove(key: Int): Element? {
        elements as MutableList
        keys as MutableList

        val index = search(key) ?: return null

        Collections.swap(elements, index, elements.size - 1)
        Collections.swap(keys, index, keys.size - 1)

        indices[keys[index]] = index
        indices[index] = null

        val last = elements.lastOrNull()

        elements.removeAt(elements.size - 1)
        keys.removeAt(keys.size - 1)

        return last
    }

    fun clear() {
        elements as MutableList
        keys as MutableList

        elements.clear()
        keys.clear()
        indices = Array(0) { null }
    }
}
