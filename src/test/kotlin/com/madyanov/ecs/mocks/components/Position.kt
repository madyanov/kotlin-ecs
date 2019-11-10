package com.madyanov.ecs.mocks.components

import com.madyanov.ecs.Component

data class Position(
    var x: Int = 0,
    var y: Int = 0
) : Component
