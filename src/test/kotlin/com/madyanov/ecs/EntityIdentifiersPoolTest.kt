package com.madyanov.ecs

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EntityIdentifiersPoolTest {

    private val pool = EntityIdentifiersPool()

    @Test
    fun `test take method`() {
        Assertions.assertEquals(0, pool.numberOfEntities)

        Assertions.assertEquals(0, pool.take().key)
        Assertions.assertEquals(1, pool.numberOfEntities)

        Assertions.assertEquals(1, pool.take().key)
        Assertions.assertEquals(2, pool.numberOfEntities)

        Assertions.assertEquals(2, pool.take().key)
        Assertions.assertEquals(3, pool.numberOfEntities)
    }

    @Test
    fun `test free method`() {
        Assertions.assertEquals(0, pool.take().key)
        Assertions.assertEquals(1, pool.take().key)
        Assertions.assertEquals(2, pool.take().key)
        Assertions.assertEquals(3, pool.numberOfEntities)

        pool.free(EntityIdentifier(1))
        Assertions.assertEquals(2, pool.numberOfEntities)
        Assertions.assertEquals(1, pool.take().key)
        Assertions.assertEquals(3, pool.numberOfEntities)
        Assertions.assertEquals(3, pool.take().key)
        Assertions.assertEquals(4, pool.numberOfEntities)
    }

    @Test
    fun `test flush method`() {
        Assertions.assertEquals(0, pool.take().key)
        Assertions.assertEquals(1, pool.take().key)
        Assertions.assertEquals(2, pool.take().key)
        Assertions.assertEquals(3, pool.numberOfEntities)

        pool.free(EntityIdentifier(1))

        pool.flush()
        Assertions.assertEquals(0, pool.numberOfEntities)
        Assertions.assertEquals(0, pool.take().key)
        Assertions.assertEquals(1, pool.take().key)
        Assertions.assertEquals(2, pool.take().key)
        Assertions.assertEquals(3, pool.numberOfEntities)
    }
}
