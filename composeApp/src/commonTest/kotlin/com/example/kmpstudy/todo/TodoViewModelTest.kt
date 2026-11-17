package com.example.kmpstudy.todo

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TodoViewModelTest {
    @Test
    fun addAppendsTodoAndClearsInput() {
        val vm = TodoViewModel()
        val before = vm.state.value.todos.size
        vm.onInputChange("  牛乳を買う  ")
        vm.add()
        val s = vm.state.value
        assertEquals(before + 1, s.todos.size)
        assertEquals("牛乳を買う", s.todos.last().title)
        assertEquals("", s.input)
    }

    @Test
    fun blankInputIsIgnored() {
        val vm = TodoViewModel()
        val before = vm.state.value.todos.size
        vm.onInputChange("   ")
        vm.add()
        assertEquals(before, vm.state.value.todos.size)
    }

    @Test
    fun toggleAndFilter() {
        val vm = TodoViewModel()
        val id = vm.state.value.todos.first { !it.done }.id
        vm.toggle(id)
        vm.setFilter(TodoFilter.Active)
        assertTrue(vm.state.value.visibleTodos.none { it.id == id })
        vm.setFilter(TodoFilter.Done)
        assertTrue(vm.state.value.visibleTodos.any { it.id == id })
    }

    @Test
    fun clearDoneRemovesOnlyDone() {
        val vm = TodoViewModel()
        vm.clearDone()
        assertTrue(vm.state.value.todos.none { it.done })
        assertTrue(vm.state.value.todos.isNotEmpty())
    }
}
