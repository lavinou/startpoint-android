[![](https://jitpack.io/v/lavinou/startpoint-android.svg)](https://jitpack.io/#lavinou/startpoint-android)
# StartPoint

StartPoint is a composable and extensible framework designed to simplify application development by providing a structured way to manage navigation, configuration, and plugin integration. The core module acts as the foundation for building scalable and customizable applications.

## Installation

StartPoint is published on JitPack as a BOM (Bill of Materials). To install, add the following to your `build.gradle` or `build.gradle.kts`:

### Step 1: Add JitPack repository
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

### Step 2: Add StartPoint BOM
```gradle
implementation(platform("com.github.lavinou.startpoint-android:bom:<latest-version>"))
```

### Step 3: Add Core Module
```gradle
implementation("com.github.lavinou.startpoint-android:core")
```

## Basic Setup

Once the core module is installed, you can initialize StartPoint by using the `rememberStartPoint` composable. This function creates and configures a `StartPoint` instance that persists across recompositions.

### Example

```kotlin
val startPoint = rememberStartPoint {
    // Configure StartPoint plugins or attributes here
}

StartPointScaffold(startPoint = startPoint) {
    // Main application content goes here
}
```

### Key Components

- **StartPoint**: The main entry point that manages the application's configuration, navigation, and plugins.
- **StartPointConfiguration**: Defines the settings and plugins that can be applied to extend the application's functionality.
- **StartPointPlugin**: Provides a mechanism to install and manage additional features within StartPoint.
- **StartPointScaffold**: A composable scaffold that integrates the StartPoint instance with the app's UI structure.

## Installing Plugins

To extend StartPoint's functionality, you can install plugins directly during configuration:

```kotlin
val startPoint = rememberStartPoint {
    install(MyCustomPlugin) {
        // Custom configuration for the plugin
    }
}
```

## Accessing Plugins

Once installed, plugins can be retrieved or checked using the following functions:

```kotlin
val pluginInstance = startPoint.pluginOrNull(MyCustomPlugin)
```

Or, to force retrieval (throws if not installed):

```kotlin
val pluginInstance = startPoint.plugin(MyCustomPlugin)
```

## Navigation

StartPoint manages navigation through `NavHostController`. Plugins can contribute to the app's navigation graph:

```kotlin
startPoint.navigation.navigate("destination")
```

Ensure that each plugin defines its own navigation graph within its implementation.

## Summary

StartPoint's core module provides a lightweight yet powerful way to manage application state, navigation, and extensibility through plugins. By leveraging composable functions and DSL configurations, it offers a seamless experience for building scalable applications.


