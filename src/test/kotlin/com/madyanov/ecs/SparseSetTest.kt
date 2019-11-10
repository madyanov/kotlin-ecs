package com.madyanov.ecs

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SparseSetTest {

    private val set = SparseSet<Int>()

    @Test
    fun `test count and isEmpty properties`() {
        val element1 = 42
        val element2 = 33

        Assertions.assertEquals(0, set.size)
        Assertions.assertTrue(set.isEmpty)

        set.insert(element1, 0)
        Assertions.assertEquals(1, set.size)
        Assertions.assertFalse(set.isEmpty)

        set.insert(element2, 1)
        Assertions.assertEquals(2, set.size)
        Assertions.assertFalse(set.isEmpty)

        set.remove(0)
        Assertions.assertEquals(1, set.size)
        Assertions.assertFalse(set.isEmpty)

        set.remove(1)
        Assertions.assertEquals(0, set.size)
        Assertions.assertTrue(set.isEmpty)
    }

    @Test
    fun `test contains method`() {
        val element = 42

        Assertions.assertFalse(set.contains(0))

        set.insert(element, 0)
        Assertions.assertTrue(set.contains(0))

        set.remove(0)
        Assertions.assertFalse(set.contains(0))
    }

    @Test
    fun `test search method`() {
        val element1 = 42
        val element2 = 33

        Assertions.assertNull(set.search(0))
        Assertions.assertNull(set.search(1))

        set.insert(element1, 0)
        Assertions.assertEquals(0, set.search(0))

        set.insert(element2, 1)
        Assertions.assertEquals(1, set.search(1))

        set.remove(0)
        Assertions.assertNull(set.search(0))
        Assertions.assertEquals(0, set.search(1))
    }

    @Test
    fun `test insert method`() {
        val element1 = 42
        val element2 = 33

        Assertions.assertTrue(set.insert(element1, 0))
        Assertions.assertTrue(set.insert(element2, 1))
        Assertions.assertEquals(2, set.size)

        Assertions.assertFalse(set.insert(element1, 0))
        Assertions.assertFalse(set.insert(element2, 1))
        Assertions.assertEquals(2, set.size)
    }

    @Test
    fun `test get method`() {
        val element1 = 42
        val element2 = 33

        Assertions.assertNull(set.get(0))
        Assertions.assertNull(set.get(1))

        set.insert(element1, 0)
        set.insert(element2, 1)
        Assertions.assertEquals(element1, set.get(0))
        Assertions.assertEquals(element2, set.get(1))

        set.remove(0)
        Assertions.assertNull(set.get(0))
        Assertions.assertEquals(element2, set.get(1))
    }

    @Test
    fun `test remove method`() {
        val element1 = 42
        val element2 = 33

        set.insert(element1, 0)
        set.insert(element2, 1)

        Assertions.assertEquals(42, set.remove(0))
        Assertions.assertEquals(1, set.size)

        Assertions.assertEquals(33, set.remove(1))
        Assertions.assertEquals(0, set.size)

        Assertions.assertNull(set.remove(2))
        Assertions.assertEquals(0, set.size)

        Assertions.assertFalse(set.contains(0))
        Assertions.assertFalse(set.contains(1))
    }

    @Test
    fun `test clear method`() {
        val element1 = 42
        val element2 = 33

        Assertions.assertTrue(set.isEmpty)

        set.insert(element1, 0)
        set.insert(element2, 1)
        Assertions.assertFalse(set.isEmpty)

        set.clear()
        Assertions.assertTrue(set.isEmpty)
    }

    @Test
    fun `test elements and keys properties`() {
        val element1 = 42
        val element2 = 33

        set.insert(element1, 0)
        set.insert(element2, 1)
        Assertions.assertEquals(listOf(42, 33), set.elements)
        Assertions.assertEquals(listOf(0, 1), set.keys)

        set.remove(0)
        Assertions.assertEquals(listOf(33), set.elements)
        Assertions.assertEquals(listOf(1), set.keys)
    }

    @Test
    fun `test iterable implementation`() {
        val element1 = 42
        val element2 = 33
        val element3 = 777

        set.insert(element1, 0)
        set.insert(element2, 1)
        set.insert(element3, 2)
        Assertions.assertEquals(listOf(42, 33, 777), set.map { it })

        set.remove(1)
        Assertions.assertEquals(listOf(42, 777), set.map { it })
    }
}
