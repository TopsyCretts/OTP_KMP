//
//  AppDelegate.swift
//  iosApp
//
//  Created by Gleb Borisov on 10.03.2026.
//


import SwiftUI
import ComposeApp


class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    var back: Back_handlerBackDispatcher = CreateBackDispatcherKt.createBackDispatcher()
    var koin: KoinApplication = InitKoinKt.doInitKoin()
    lazy var root: RootComponent = CreateRootComponentKt.createRootComponent(
        componentContext: DefaultComponentContext(
            lifecycle: ApplicationLifecycle(),
            stateKeeper: nil,
            instanceKeeper: nil,
            backHandler: back
        ),
        koinApplication: koin
    )
}
