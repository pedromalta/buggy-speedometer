import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        InjectableModulesKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}