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
1. Clone the repo: `git clone https://github.com/Barkha-Yadav/HabitTracker.git`
2. Open the folder in Android Studio and wait for Gradle sync to finish.
3. Select a device (phone or emulator) in the toolbar.
4. Click Run.

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