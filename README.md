# Movie Android App

A modern Android application built with Jetpack Compose that displays trending movies and allows users to search for movies. **All business logic is consumed from an external shared KMM library** (`com.example.movieapp:shared`), keeping this Android project focused purely on UI presentation.

## Key Architecture

- **External Shared Logic**: Consumes business logic from `com.example.movieapp:shared` KMM library
- **Native Android UI**: Pure Android implementation using Jetpack Compose
- **Clean Separation**: Android app handles only UI presentation while business logic comes from shared library
- **Dependency-Based**: Uses the shared library as an external dependency

## Features

- **Trending Movies**: Browse a list of currently trending movies
- **Movie Search**: Search for movies by title with real-time results
- **Movie Details**: View comprehensive information about selected movies
- **Network Status**: Real-time network connectivity monitoring
- **Modern UI**: Material 3 design system with Jetpack Compose
- **Offline Support**: Caching handled by shared library

## Tech Stack

### Android (This Project)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Navigation**: Jetpack Navigation Compose
- **Image Loading**: Coil
- **Dependency Injection**: Koin (Android module)
- **Minimum SDK**: 24 (Android 7.0)

### External Dependencies
- **Shared Library**: `com.example.movieapp:shared` - Contains all business logic, networking, data management, and domain models
- **Business Logic**: Repository pattern, use cases, and data handling from shared library
- **API Integration**: Network calls and data parsing handled by shared library

## Project Structure

```
MovieAndroid/
├── app/                           # Main Android application module
│   └── src/main/java/            # Android-specific UI code
├── core/feature/                 # Core Android UI components and utilities
├── feature/movies/               # Movie feature UI module
│   └── presentation/             # ViewModels and Compose UI components
├── navigation/                   # Android navigation components
└── build.gradle.kts              # Dependencies including shared library
```

## Dependencies

The project consumes shared business logic via:

```kotlin
dependencies {
    implementation("com.example.movieapp:shared:$sharedVersion")
    // Other Android-specific dependencies...
}
```

## Architecture Benefits

- **Separation of Concerns**: UI logic separated from business logic
- **Maintainability**: Business logic updates come from shared library updates
- **Focus**: Android developers can focus purely on UI/UX implementation
- **Consistency**: Same business logic can be used across multiple platforms
- **Modularity**: Clean dependency management through external library

## Development Notes

- **UI Development**: Focus on Jetpack Compose UI components and Android-specific features
- **Business Logic**: All business logic comes from the external shared library - update the library version to get new features
- **Data Flow**: ViewModels consume use cases and repositories from the shared library
- **Testing**: Focus on UI testing and integration testing with the shared library

## Dependency Management

- **Shared Library Updates**: Update the `shared` version in `libs.versions.toml` to get latest business logic
- **Android Dependencies**: Manage Android-specific UI and platform dependencies separately
- **Version Compatibility**: Ensure Android app version is compatible with shared library version

## Project Relationship

```
┌─────────────────────────┐
│   Shared KMM Library    │
│ (com.example.movieapp)  │ ←─── Separate Repository
│   - Business Logic      │      (Contains all logic)
│   - Networking         │
│   - Data Management    │
└─────────────────────────┘
            ↓ (dependency)
┌─────────────────────────┐
│   Movie Android App     │ ←─── This Repository
│ (Native Android)        │      (UI Only)
│   - Jetpack Compose    │
│   - UI Components      │
│   - Navigation         │
└─────────────────────────┘
```

