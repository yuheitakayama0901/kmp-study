package com.example.kmpstudy.todo

data class Todo(
    val id: Long,
    val title: String,
    val done: Boolean = false,
)

enum class TodoFilter(val label: String) {
    All("すべて"),
    Active("未完了"),
    Done("完了"),
}

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val filter: TodoFilter = TodoFilter.All,
    val input: String = "",
) {
    val visibleTodos: List<Todo>
        get() = when (filter) {
            TodoFilter.All -> todos
            TodoFilter.Active -> todos.filterNot { it.done }
            TodoFilter.Done -> todos.filter { it.done }
        }

    val remainingCount: Int
        get() = todos.count { !it.done }

    val canAdd: Boolean
        get() = input.isNotBlank()
}
