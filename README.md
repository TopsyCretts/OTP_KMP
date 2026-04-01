# TopTemplate - Kotlin Multiplatform Project

This project is a high-quality Kotlin Multiplatform (KMP) template for Android and iOS, following modern architectural patterns.

## 🏗 Project Structure

The project is organized into several modules to ensure clear separation of concerns:
- **`androidApp`**: The Android entry point and configuration.
- **`iosApp`**: The Swift-based iOS entry point and configuration.
- **`composeApp`**: KMP&CMP entry point. Contains the shared Compose Multiplatform UI and Logic.
- **`modules/`**: Contains rest modules.
    - **`core`**: Base classes, utilities, and core logic shared across modules.
    - **`main`**: The main feature module, including presentation and domain logic.
- **`buildSrc`**: Contains custom rename package Gradle plugin.

## 🛠 Tech Stack

- **Kotlin Multiplatform**: Shared logic between Android and iOS.
- **Compose Multiplatform**: Shared UI framework.
- **Decompose**: Used for navigation and component lifecycle management.
- **MVIKotlin**: Predictable state management following the MVI pattern.
- **Koin**: Dependency injection.
- **Coroutines & Flow**: Asynchronous programming and reactive data streams.

## 🚀 Key Features

### Gradle Package Rename Plugin
The project includes a custom Gradle plugin to easily rename packages across all modules. This is particularly useful when starting a new project from this template.

**Usage:**
1. Configure the new package name in your root `build.gradle.kts` (if not already configured):
   ```kotlin
   packageRename {
       newPackage = "com.your.new.package"
   }
   ```
2. Run the rename task from the terminal:
   ```shell
   ./gradlew renameAllPackages
   ```
This will automatically update the package declarations and directory structures in all subprojects.

### Dependency Injection (Koin)
DI is initialized platform-specifically to handle platform-specific dependencies:
- **Android**: Initialized in `MyApplication` (or equivalent).
- **iOS**: Initialized via `initKoin` in `composeApp/src/iosMain/init`.

The `appModule` in `composeApp` serves as the central point for dependency declarations, including feature modules like `mainModule`.

### Navigation (Decompose)
The navigation is handled by **Decompose**. The `RootComponent` manages the navigation stack and provides component contexts to child components. This ensures that the state and lifecycle are handled correctly across configuration changes (on Android) and platform transitions.

### State Management (MVIKotlin)
We use **MVIKotlin** to manage UI state. Each component typically has a `Store` that handles:
- **Intent**: Actions from the user.
- **State**: The current UI state.
- **Label**: One-time events (like navigation or showing a toast).

The `StoreFactory` (using `DefaultStoreFactory`) is provided via Koin.

## 📱 Getting Started

### Android
1. Open the project in Android Studio.
2. Select the `androidApp` run configuration.
3. Click "Run".

### iOS
1. Open `iosApp/iosApp.xcworkspace` in Xcode (after running `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode`).
2. Select your target device/simulator.
3. Click "Run".

Alternatively, you can run directly from Android Studio if you have the Kotlin Multiplatform plugin installed.
