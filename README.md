# Spotlight
## Description
A simple productivity app written in Kotlin that enables users to focus on one task at a time

## How to use
Feel free to clone the repository and build the app from Android Studio to start your Spotlight journey!

## Features
1. Hyper focus - shows a user the current task being done to keep user's attention to one task at a time
2. Daily Intent List - lists down only a limited number of tasks to keep users focused on what is needed to be finished within the day
3. Task alert - Reminds the user what is needed to be done to avoid drifting off to other things

## Technologies and Architectures Used
1. Jetpack Compose and Fragments - for UI and animations
2. ViewModel with LiveData - for presenter logic
3. Room Database - for local storage
4. Coroutines and Flows - for asynchronous operations and data sources
5. Unidirectional data flow architecture - for overall architecture of UI and data layer
6. Hilt - for dependency injection
7. WorkManager - for daily refresh of tasks