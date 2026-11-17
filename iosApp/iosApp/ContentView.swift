import SwiftUI
import UIKit
import ComposeApp

/// Kotlin 側の MainViewController() (Compose の画面) を SwiftUI に埋め込む
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea() // 余白(ステータスバー・キーボード)は Compose 側で処理する
    }
}
