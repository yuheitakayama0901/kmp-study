package com.example.kmpstudy.todo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Android / iOS 共通の ViewModel。
 * androidx.lifecycle の KMP 版 (org.jetbrains.androidx.lifecycle) を使っている。
 */
class TodoViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        TodoUiState(
            todos = listOf(
                Todo(id = 1, title = "KMP のプロジェクト構成を眺める", done = true),
                Todo(id = 2, title = "commonMain に画面を書いてみる"),
                Todo(id = 3, title = "iOS シミュレータで動かす"),
            ),
        ),
    )
    val state: StateFlow<TodoUiState> = _state.asStateFlow()

    private var nextId = 4L

    fun onInputChange(text: String) {
        _state.update { it.copy(input = text) }
    }

    fun add() {
        val title = _state.value.input.trim()
        if (title.isEmpty()) return
        _state.update {
            it.copy(
                todos = it.todos + Todo(id = nextId++, title = title),
                input = "",
            )
        }
    }

    fun toggle(id: Long) {
        _state.update { s ->
            s.copy(todos = s.todos.map { if (it.id == id) it.copy(done = !it.done) else it })
        }
    }

    fun delete(id: Long) {
        _state.update { s -> s.copy(todos = s.todos.filterNot { it.id == id }) }
    }

    fun clearDone() {
        _state.update { s -> s.copy(todos = s.todos.filterNot { it.done }) }
    }

    fun setFilter(filter: TodoFilter) {
        _state.update { it.copy(filter = filter) }
    }
}
