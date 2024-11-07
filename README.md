# Kwise Android #

## Architecture ##

The project uses a Modern App Architecture which is organized in these layers:

- **App**: "UI layer" (or presentation layer) which displays the application data on the screen. Contains the UI elements that render the data on the screen, state holders (such as ViewModel classes) that hold data, expose it to the UI, and handle logic.
	- The UI Layer uses MVVM and Jetpack Compose.

- **Data**: The data layer is made of repositories that each can contain zero to many data sources. Also contains the API description and model.

- **Domain**: it is an optional layer that sits between the UI and data layers. The domain layer is responsible for encapsulating complex business logic, or simple business logic that is reused by multiple ViewModels.


## Requirements ##

- Target Android SDK 35.
- Minimal Android SDK 28.
- Kotlin.
- Android Studio 2024.2.1
