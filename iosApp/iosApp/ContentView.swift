import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    let root: RootComponent
    let back: Back_handlerBackDispatcher
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(rootComponent: root, backDispatcher: back)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    let root: RootComponent
    let back: Back_handlerBackDispatcher

    var body: some View {
        ComposeView(root: root, back: back)
            .ignoresSafeArea(.keyboard)
            .ignoresSafeArea(edges: .all)
    }
}



