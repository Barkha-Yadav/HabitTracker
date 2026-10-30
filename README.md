# Habit Tracker

A simple Android app to maintain a list of daily habits and mark them as done each day. Built as a Month 1 practice project during my Android internship.

## Purpose
Learn Kotlin and basic Android development (XML layouts, Activity lifecycle, Git) by building a small app that works offline.

## Prerequisites
- Android Studio (latest stable)
- Android SDK, API 35
- A phone with USB debugging on, or an emulator
- Git

## How to run

1. Clone the repository: `git clone https://github.com/Barkha-Yadav/HabitTracker.git`
2. Open the project in Android Studio and wait for the Gradle sync to finish.
3. On your phone, turn on **Developer options** and then **USB debugging**.
4. Connect the phone to the computer with a USB cable and tap **Allow** on the phone when it asks to trust the computer.
5. Select your phone in the device drop-down at the top of Android Studio.
6. Click the green **Run** button.

Tested on: vivo V2240, Android 15 (API 35), using USB debugging.

## Features
Done so far:
- Habit data class (name, isCompleted)
- Sample habits printed to Logcat

Planned:
- Show the habit list on screen
- Add a habit (blank names rejected)
- Mark / unmark a habit as done today
- Edit and delete a habit
- Local storage (once the basics are stable)

## Known limitations
- Data is in memory only, so it is lost when the app closes
- No login, cloud sync or notifications (not required in Month 1)

## Tech
Kotlin, XML layouts, Android SDK, Git