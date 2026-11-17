package com.example.kmpstudy

import androidx.compose.ui.window.ComposeUIViewController

/** Swift 側から呼ばれる。Compose の画面を UIViewController として返す */
fun MainViewController() = ComposeUIViewController { App() }
