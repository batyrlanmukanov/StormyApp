Stormy: Modern Android Weather Application
This project is a fully functional weather monitoring application developed to fulfill all technical requirements of the final examination. Stormy demonstrates the mastery of the modern Android development stack, emphasizing stability, clean architecture, and user experience.

Project Scope and Technologies
The application fully implements the required technologies for the highest score:

Architecture (MVVM): The project follows a strict MVVM pattern, ensuring a clear separation of concerns between the View (Fragments), the business logic (ViewModel), and the data layer (Repository).

Networking (Retrofit & Coroutines): All communication with the external OpenWeatherMap API is handled by Retrofit. Network calls are managed asynchronously and safely using Kotlin Coroutines (launched via viewModelScope) to guarantee a non-blocking and smooth user interface.

Offline Mode (Room Database & SharedPreferences): The application adopts an Offline-First strategy. All received weather data is persisted in a local Room Database cache. The UI consumes data reactively via Flow, ensuring that the app displays the latest cached data immediately, even without internet access. User preferences, such as the Dark Mode state, are handled by SharedPreferences.

Key Features
The core functionality ensures a production-level experience:

Single Source of Truth: The Room Database acts as the primary data source, guaranteeing data consistency across the application.

Dynamic Theming: Full support for Dark Mode, which can be toggled via the Settings screen using AppCompatDelegate for system-wide theme changes.

Robust Navigation: Navigation between the main screen and settings is managed by the Jetpack Navigation Component.

How to Run the Project
Clone the repository.

Obtain a free API key from OpenWeatherMap.

Insert the API key into the designated constant file (e.g., Api.kt).

Run the project on an emulator or physical device.
