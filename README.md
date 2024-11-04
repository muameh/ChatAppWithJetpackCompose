# ChatterApp

**ChatterApp** is a modern, real-time messaging application developed with Kotlin and Jetpack Compose. This app provides a seamless chat experience using Firebase for real-time messaging and authentication, implementing the latest Android development practices.

## Features

- **Real-Time Messaging**: Instantly receive and send messages using Firebase Realtime Database.
- **Firebase Authentication**: Secure user authentication and sign-up.
- **Push Notifications**: Receive push notifications for new messages through Firebase Cloud Messaging.
- **Jetpack Compose UI**: Built entirely with Jetpack Compose, creating a responsive, smooth, and engaging user experience.
- **Efficient Navigation**: Structured navigation with parameterized screen transitions.
- **Dependency Injection**: Uses Dagger Hilt for cleaner, modularized code.

## Architecture

The application is based on **MVVM architecture** combined with a **clean architecture** pattern. Key components include:

- **MVVM (Model-View-ViewModel)**: Separates concerns to enhance code readability and testing.
- **Repository Pattern**: Centralizes data management and abstracts data sources, making the app robust and maintainable.
- **Firebase Messaging Service**: Manages background notifications, displaying messages via `NotificationCompat`.
- **Custom Navigation Setup**: Parameterized navigation using `NavController`, enabling seamless screen transitions.

## Key Modules & Components

### Data Models

- **Channel**: Represents a chat channel, with properties such as `id`, `name`, and `createdAt` initialized with timestamps.
- **Message**: Encapsulates message details including `senderId`, `receiverId`, `message`, and `timestamp`.

### Screens

- **LogInScreen** and **SignUpScreen**: Firebase Authentication integration for user sign-in and registration.
- **HomeScreen**: Displays available channels, search functionality, and an option to add new channels.
- **ChatScreen**: Real-time chat interface displaying message bubbles with user-specific styling.

### UI Components

- **ChannelCard**: Composable for presenting individual channels in a list format, with clickable navigation to specific chat screens.
- **ChatBubble**: Custom chat bubble style with unique designs for sent and received messages.

### Firebase Integration

- **Firebase Authentication**: Manages secure user login and registration.
- **Firebase Realtime Database**: Provides real-time updates for channels and messages.
- **Firebase Cloud Messaging (FCM)**: Delivers notifications for new messages.

## Setup & Installation

1. **Clone the repository**:
   ```bash
   https://github.com/muameh/ChatAppWithJetpackCompose.git
