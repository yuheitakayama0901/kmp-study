package com.example.kmpstudy

import androidx.compose.runtime.Composable
import com.example.kmpstudy.theme.AppTheme
import com.example.kmpstudy.todo.TodoScreen

/** Android / iOS 共通のエントリポイント */
@Composable
fun App() {
    AppTheme {
        TodoScreen()
    }
}
