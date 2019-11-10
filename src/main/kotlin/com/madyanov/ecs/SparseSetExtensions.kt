package com.madyanov.ecs

fun <T: Identifiable<Int>> SparseSet<out T>.contains(element: T) = search(element) != null
fun <T: Identifiable<Int>> SparseSet<out T>.search(element: T): Int? = search(element.id)
fun <T: Identifiable<Int>> SparseSet<in T>.insert(element: T): Boolean = insert(element, element.id)
fun <T: Identifiable<Int>> SparseSet<out T>.remove(element: T): T? = remove(element.id)
