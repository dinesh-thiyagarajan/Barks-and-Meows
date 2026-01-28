# Barks and Meows

A **Kotlin Multiplatform** pet management app for Android and iOS that helps pet owners track their pets' information and vaccination history.

Built with Jetpack Compose, Firebase, and clean architecture principles.

<p align="center">
  <img src="https://github.com/user-attachments/assets/c5126e71-04d4-47f1-8423-3e4a7365951c" alt="Login Screen" width="250"/>
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/f72b6490-1604-4671-822f-326f6e2ab61b" alt="Pet List Screen" width="250"/>
  &nbsp;&nbsp;&nbsp;
  <img src="https://github.com/user-attachments/assets/9e340a0f-b389-4a6b-9409-bef49925e206" alt="Pet Detail Screen" width="250"/>
</p>

| Login | Pet List | Pet Detail |
|:---:|:---:|:---:|
| Sign in with email/password or Google | View all your pets at a glance | Track vaccine records per pet |

## Features

- **Authentication** - Email/password login, Google Sign-In, password reset, and account deletion
- **Pet Management** - Add, edit, and delete pets with categories (Dog, Cat, etc.) and photos
- **Vaccine Tracking** - Record vaccination history with vaccine name, doctor info, date, and notes
- **Cross-Platform** - Shared business logic via Kotlin Multiplatform targeting both Android and iOS

## Tech Stack

| Category | Technology |
|---|---|
| **Language** | Kotlin 2.3.0 |
| **Framework** | Kotlin Multiplatform (KMP) |
| **UI** | Jetpack Compose + Material 3 |
| **Navigation** | Navigation Compose (type-safe) |
| **DI** | Koin 4.1.1 |
| **Backend** | Firebase Auth + Firestore |
| **Auth** | Google Sign-In, Firebase Auth |
| **Async** | Kotlin Coroutines |

## Architecture

The project follows **Clean Architecture** with the **MVVM + Repository** pattern, organized into feature modules:

```
Barks-and-Meows/
├── composeApp/       # Main app module (UI screens, navigation, theming)
├── auth/             # Authentication module (login, signup, profile)
├── pet/              # Pet management module (CRUD operations)
├── vaccine/          # Vaccine tracking module
├── uiComponents/     # Shared reusable UI components
├── iosApp/           # iOS app entry point
└── docs/             # Documentation and policies
```

Each feature module is structured as:

```
module/
├── dataSource/       # Firebase Firestore interactions
├── repository/       # Repository interfaces and implementations
├── useCases/         # Business logic
├── viewModels/       # State management
└── dataModels/       # Data classes
```

## Getting Started

### Prerequisites

- Android Studio Ladybug or later
- JDK 18+
- A Firebase project with Auth and Firestore enabled

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/dinesh-thiyagarajan/Barks-and-Meows.git
   ```

2. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)

3. Enable **Email/Password** and **Google Sign-In** authentication providers

4. Enable **Cloud Firestore** database

5. Download and add your Firebase config files:
   - **Android**: Place `google-services.json` in `composeApp/`
   - **iOS**: Place `GoogleService-Info.plist` in `iosApp/`

6. For Google Sign-In setup, refer to the [Google SSO Setup Guide](docs/GOOGLE_SSO_SETUP.md)

7. Build and run the project in Android Studio

### Build

```bash
./gradlew assembleDebug
```

## Requirements

- **Android**: Min SDK 24 (Android 7.0) | Target SDK 36
- **iOS**: Configured via Xcode project in `iosApp/`

## Contributing

Contributions are welcome! Feel free to:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## Privacy Policy

See the [Privacy Policy](docs/privacy_policy.md) for details on data collection and usage.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
