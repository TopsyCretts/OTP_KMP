# DecomposeOTP - Kotlin Multiplatform OTP Sample

This project is a high-quality Kotlin Multiplatform (KMP) sample demonstrating a robust, production-ready OTP (One-Time Password) input component. It follows modern architectural patterns using **Decompose** for lifecycle-aware component management and **MVIKotlin** for predictable state management.

## 🏗 Project Structure

The project is organized into modular layers to ensure clear separation of concerns:
- **`composeApp`**: The shared entry point containing the main UI and navigation root.
- **`modules/core`**: Contains the core OTP logic, base classes, and shared presentation components.
- **`modules/main`**: The feature module implementing the main screen that utilizes the OTP component.
- **`androidApp`**: Android-specific entry point.
- **`iosApp`**: Swift-based iOS wrapper.

## 🛠 Tech Stack

- **Kotlin Multiplatform**: Shared logic between Android and iOS.
- **Compose Multiplatform**: Shared UI framework for consistent look and feel.
- **Decompose**: Component-based navigation and lifecycle management.
- **MVIKotlin**: State management following the Model-View-Intent pattern.
- **Koin**: Dependency injection across all platforms.
- **Coroutines & Flow**: Reactive programming and asynchronous event handling.

## 🔑 OTP Component Architecture

The heart of this repository is the shared OTP component, which provides a seamless user experience with automated focus management and input handling.

### 🧩 OtpCodeComponent (Decompose)
The `OtpCodeComponent` is a lifecycle-aware component that acts as the controller for the OTP input. 
- **State**: Exposes a `Value<OtpCodeStore.State>` which the UI observes.
- **Output**: Communicates events (like `CodeFilled`) back to parent components.
- **Interactions**: Defines clear methods for user actions: `onNumberEnter`, `onFieldFocused`, and `onKeyboardBack`.

### 🧠 OtpCodeStore (MVIKotlin)
The business logic resides in `OtpCodeStore`, ensuring the OTP behavior is identical on all platforms.
- **Intents**: Handles inputting numbers, manual focus changes, and backspace events.
- **State**: Maintains the list of entered digits and the currently focused index.
- **Logic (Executor)**: 
    - **Auto-focus**: Automatically moves to the next empty field after a digit is entered.
    - **Backspace Navigation**: Intelligently moves focus back and clears fields when the user deletes input.
    - **Completion**: Triggers a `CodeFilled` label once all fields are populated.

## 💡 The Key Idea: The "Zero-Width" Solution

A major challenge in Compose Multiplatform (CMP) is reliably detecting **backspace/delete** events on digital keyboards. Standard `onKeyEvent` listeners often fail to trigger for soft keyboards on iOS and certain Android devices (see [CMP-7868](https://youtrack.jetbrains.com/issue/CMP-7868)).

To solve this, this sample implements a clever logic based on **Zero-Width Characters** and **State Comparison**.

### How it works:
Instead of relying on unstable key events, we compare the **Old Value** and **New Value** inside `onValueChange`:

1.  **Hidden Character**: Every "empty" field is initialized with a `ZeroWidthChar` (`\u200B`).
2.  **Cursor Positioning**: The cursor is always forced to stay at the second position (`index = 1`), right after the hidden character.
3.  **The Comparison Logic**:
    *   **Case 1: Field was empty (contained only `\u200B`)**
        *   If `newText` has a digit at the end -> **User entered a number**.
        *   If `newText` is empty (the `ZeroWidthChar` was deleted) -> **User pressed Backspace**. This is our reliable hook for back-navigation!
    *   **Case 2: Field was filled (contained a digit)**
        *   If `newText` is empty or only has the `ZeroWidthChar` -> **User cleared the field**.

### Code Example (`CodeButton.kt`):
```kotlin
private const val ZeroWidthChar = '\u200B'

// Force the text field to always have the hidden char when "empty"
var text by remember(number) {
    mutableStateOf(
        TextFieldValue(
            text = number?.toString() ?: ZeroWidthChar.toString(),
            selection = TextRange(index = 1) // Cursor after the char
        )
    )
}

BasicTextField(
    value = text,
    onValueChange = { newValue ->
        val oldText = text.text
        val newText = newValue.text
        
        // Key Idea: Compare Old vs New
        if (oldText.all { it == ZeroWidthChar }) { 
            val newChar = newText.lastOrNull() ?: ZeroWidthChar
            if (newChar.isDigit()) {
                onNumberChanged(newChar.digitToIntOrNull())
            } else if (newChar == ZeroWidthChar) {
                // The hidden char was deleted! 
                // This works on ALL devices including iOS soft keyboards.
                onKeyboardBack() 
            }
        } else {
            if (newText.lastOrNull() == null) {
                onNumberChanged(null)
            }
        }
    }
)
```

This approach ensures that the OTP input remains functional and responsive across all platforms, bypassing the limitations of hardware-specific event listeners.

## 🚀 Key Features

- **Platform Agnostic**: 100% of the OTP logic is shared.
- **Reliable Backspace**: Cross-platform deletion logic that works on iOS and Android soft keyboards.
- **Production Ready**: Handles edge cases like middle-field editing and rapid typing.
- **Decompose Integration**: Easily nested within any navigation stack.

## 📱 Getting Started

### Android
1. Open the project in Android Studio.
2. Select the `androidApp` run configuration and click "Run".

### iOS
1. Ensure you have Xcode installed.
2. Run `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode`.
3. Open `iosApp/iosApp.xcworkspace` in Xcode and run the project.

---
*This project was evolved from a template to showcase advanced KMP patterns and solve real-world CMP issues.*
