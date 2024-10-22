# Forecast App

The **Forecast App** is a user-friendly Android application designed to provide real-time weather forecasts based on user-selected cities. The app utilizes modern Android development practices and technologies, ensuring a smooth, efficient, and responsive user experience.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [ViewModel and State Management](#viewmodel-and-state-management)
- [Acknowledgments](#acknowledgments)

## Features

- **City Search**: Easily search for cities using a dedicated search bar, with instant suggestions as you type.
- **Detailed Weather Forecasts**: View comprehensive weather information, including temperature, humidity and other relevant metrics for the selected city.
- **Loading Indicators**: Visual feedback during data fetching, keeping users informed about the app's status.
- **Error Handling**: Gracefully handle errors from network requests with descriptive messages and a retry option, enhancing user experience.
- **Clean Architecture**: Follows the principles of clean architecture to maintain code organization, testability, and scalability.
- **Modern UI**: Built using Jetpack Compose for a responsive and modern user interface.

## Technologies Used

- **Kotlin**: The primary programming language for Android development, enabling concise and expressive code.
- **Jetpack Compose**: A declarative UI toolkit that allows developers to build UIs with less boilerplate and better performance.
- **Coroutines**: Used for asynchronous programming, simplifying the code for managing long-running tasks like network requests.
- **Hilt**: A dependency injection library for Android that simplifies the process of providing dependencies in the app.
- **StateFlow**: A reactive data flow type for managing UI states in a lifecycle-aware manner, allowing easy handling of UI updates.

## Architecture

- The application is structured following a clean architecture pattern, divided into the following components:

- **Presentation Layer**: : Contains the UI components and ViewModels that manage UI-related logic and state.
- **Data Layer**: : Responsible for data retrieval from various sources, including APIs for weather data and local databases for caching city information.
  ViewModel and State Management
  The HomeViewModel plays a crucial role in managing the application's state and handling user interactions:

## ViewModel and State Management

- **It observes a list of cities from the CityRepository.**
- **Upon city selection, it fetches the weather forecast from the ForecastRepository.**
- **The state of the UI is managed through a sealed class HomeUiState, representing different states (loading, success, error) for better clarity in handling UI updates.**
- **A separate state management flow for the forecast is implemented to ensure responsive feedback during data fetching and error recovery.**

## Acknowledgments

- **Weather data provided by OpenWeatherMap.**
