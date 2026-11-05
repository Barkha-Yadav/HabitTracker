# Notes: HabitTracker

Stack: Kotlin, Android Studio, XML layouts, Android SDK, Git

---

# Part 1: Android basics

## 1.1 Project setup
- Test device: vivo V2240, Android 15 (API 35), connected by USB debugging.
- Project: HabitTracker, package `com.example.habbittracker` (note the double "b"), Empty Views Activity, Kotlin.
- Gradle sync downloads libraries and prepares the build. It must succeed before the app can run.
- I use XML layouts (as per the internship plan), not Jetpack Compose.
  - XML: the UI is written in a layout file and connected to Kotlin code.
  - Compose: the UI is written in Kotlin code. Not used in this plan.

## 1.2 Project structure

### MainActivity.kt
- Holds the Kotlin logic for the first screen (an Activity = one screen).
- `onCreate()` runs when the screen is created. Android calls it automatically.
- `setContentView(R.layout.activity_main)` connects this screen to its XML layout.
- The edge-to-edge and insets lines come from the template. They add padding so content doesn't hide under the status and navigation bars. They need the root layout to have the id `main`.

### activity_main.xml
- Defines the screen's UI (what the user sees) in XML.
- The template started with a `ConstraintLayout` (id `main`). In Week 2 I changed the root to a vertical `LinearLayout` (still with id `main`). See 4.1.
- Each view needs `layout_width` and `layout_height`.
  - `match_parent`: as big as the parent.
  - `wrap_content`: only as big as the content.
- Use `sp` for text size, `dp` for other sizes.
- `xmlns:android`, `xmlns:app`, `xmlns:tools` are namespace prefixes: `android:` for standard attributes, `app:` for library attributes (such as constraints), `tools:` for preview-only values.
- Design view shows the real look; Blueprint view shows only the outlines.

### strings.xml
- Stores the app's text in one place (`res/values`).
- Referenced in layouts as `@string/name` instead of hardcoding text.
- Makes text easy to reuse, change and translate.
- Tested it with `greeting` ("Good Morning!") and `textSize="32sp"`.
- Week 2 strings: `app_title`, `habit_name_hint`, `add_habit`.

### AndroidManifest.xml
- The app's declaration file. Android reads it to learn what the app contains.
- Every Activity must be registered here, or it can't open.
- The `intent-filter` with `MAIN` + `LAUNCHER` marks MainActivity as the screen that opens when the app icon is tapped.
- `android:exported="true"` lets the launcher (another app) start this Activity.
- `windowSoftInputMode` controls how the screen behaves when the keyboard opens.

### build.gradle.kts (Module :app)
- Build settings for the app, read during Gradle sync.
- `namespace` / `applicationId`: the app's package name.
- `minSdk`, `targetSdk`, `compileSdk`: the Android versions supported and built against.
- `dependencies { }`: the libraries the app uses. Room and Retrofit will be added here later.

### Drawable XML
- Vector and shape drawables are drawing instructions, not pixels, so they stay sharp at any size.

### How the files connect
`AndroidManifest.xml` launches `MainActivity.kt` → `setContentView` loads `activity_main.xml` → the layout reads text from `strings.xml`.

## 1.3 Resource symbols and the R class
- `@string/name`, `@color/name`, `@drawable/name`: use something that already exists.
- `@+id/name` (only for ids) creates a new id when defining a view. `@id/name` refers to an existing one.
- `id` is a resource type, not a file. Android generates the `R` class at build time, and `@+id/greetingText` adds `R.id.greetingText` to it. Never edit `R`.
- In Kotlin: `R.id.name` for views, `R.string.name` for texts.

## 1.4 Connecting XML and Kotlin
- Give a view an id in XML, then find it in Kotlin: `findViewById<TextView>(R.id.greetingText)`.
- Call this after `setContentView`, because the layout must be loaded first.
- `setOnClickListener { ... }` runs code when the view is tapped.
- `textView.text = "Taps: $count"` changes the displayed text. `$count` inserts a variable into a string.
- Tap-counter demo: each tap increases `count` and updates the TextView.

## 1.5 Activity lifecycle
- Order: onCreate → onStart → onResume → (running) → onPause → onStop → onDestroy.
- Open app: onCreate, onStart, onResume.
- Home button: onPause, onStop. Reopening: onStart, onResume (no onCreate).
- Back button: onPause, onStop, onDestroy.
- Lifecycle functions are separate functions inside the class, after `onCreate`, not inside it.
- Keep `super.onXxx(...)` as the first line of each lifecycle function.

## 1.6 Rotation
- Rotating the phone destroys and recreates the Activity (onDestroy then onCreate).
- Plain variables inside the Activity are lost on rotation (e.g. a `count` goes back to 0).
- In the Habit Tracker, the habit list goes back to the sample habits after rotation, because it lives only in memory.
- Fixes learned later: ViewModel (survives rotation) and Room (saves data permanently).

## 1.7 Logcat
- Logcat shows live messages from the device (system and app). Open it from the Logcat tool window and select the phone at the top.
- Log functions:
  - `Log.v("tag", "message")`: verbose
  - `Log.d("tag", "message")`: debug
  - `Log.i("tag", "message")`: info
  - `Log.w("tag", "message")`: warning
  - `Log.e("tag", "message")`: error
- The first value is the tag, the second is the message. Example: `Log.d("MainActivity", "onCreate() is called")`.
- `import android.util.Log` tells Kotlin where `Log` lives. Without it: "Unresolved reference". Alt+Enter adds imports automatically.
- Filters (search box): `package:mine` (my app only), `tag:MainActivity` (my own tag), `level:debug` (Debug and above).
- The letter beside each line is its level: V, D, I, W, E (Error, red).
- Place `Log.d` below `super.onCreate(...)`.

## 1.8 Git
- `git config user.name` / `user.email`: set my identity (done once).
- `git init`: create a repository.
- `git status`: see what changed.
- `git add .`: stage changes for the next commit. `git add README.md` stages one file.
- `git commit -m "message"`: save a snapshot with a meaningful message.
- `git commit -am "message"`: stage and commit changes to already tracked files (does not add new files).
- `git push`: send commits to GitHub.
- `git log --oneline`: see history.
- `.gitignore` keeps build files, local settings and secrets out of Git.
- Repository: https://github.com/Barkha-Yadav/HabitTracker (branch `main` tracks `origin/main`).
- Commit and push regularly. Never commit passwords or API keys.

## 1.9 README.md
- The README is the front page of the GitHub repository. It is created at the project root (Project view, right-click HabitTracker, New > File).
- Sections: purpose, prerequisites, how to run, features (done and planned), known limitations, tech.
- "How to run" must say how I test: clone, open in Android Studio, wait for Gradle sync, turn on USB debugging, tap Allow on the phone, pick the device, press Run. Add "Tested on: vivo V2240, Android 15 (API 35)".
- Update it whenever a feature is finished (move it from Planned to Done).
- Markdown basics: `#` heading, `-` bullet, `1.` numbered list, `**bold**`, backticks for code, `|` for tables.
- Preview button (top right of the editor) shows how it will look on GitHub.
- The plan requires a README with setup steps, completed features and known issues.

---

# Part 2: Kotlin exercises

Practised in https://play.kotlinlang.org

## 2.1 Variables and types
- `val` = read-only (cannot be reassigned); `var` = can be changed. Start with `val`, use `var` only when needed.
- Basic types: `String` (text), `Int` (whole number), `Double` (decimal), `Boolean` (true/false).
- Type inference: Kotlin guesses the type from the value, e.g. `val x = 5` is Int.
- Explicit type is written after a colon: `val x: Int = 5`.
- String templates: `"Hello $name"` and `"Total: ${a + b}"` put values inside text.
- Use `${ }` whenever there is a dot or a calculation: `"${habit.name}"`.

## 2.2 Conditions
- `if/else` runs code depending on a condition; `else if` handles more choices.
- `=` stores a value, `==` compares two values.
- `&&` = AND, `||` = OR, `!` = NOT.
- `if` can return a value: `val status = if (done) "Completed" else "Pending"`.
- Kotlin has no `? :` ternary. Use `if (cond) a else b`.
- A Boolean needs no `== true`: write `if (h.isCompleted)`.
- `when` is a cleaner way to handle many choices; `when { cond -> result }` works like many else-ifs.
- In `when`, use `in 1..6 ->` to check a range (without `in`: "Incompatible types Int and IntRange").
- `text.isBlank()` is true when text is empty or only spaces (used to validate habit names).
- `text.isNotBlank()` is the opposite: true when the text has real content. Handy in validation: `name.isNotBlank() && name.length <= 30`.
- `println` prints and moves to a new line; `print` stays on the same line.
- A function that returns a value does not print it. Use `println(function())` to see the result.

## 2.3 Loops and ranges
- `1..5` = 1,2,3,4,5 (includes the end); `1 until 5` = 1,2,3,4 (stops before the end).
- `for (i in 1..5) { ... }` repeats the code once for each number.
- `step` jumps by a number (`0..10 step 2`); `downTo` counts backwards (`5 downTo 1`).
- `while (condition) { ... }` repeats as long as the condition is true. Forgetting to change the variable causes an infinite loop.
- `count++` is short for `count = count + 1`.
- Use `for` for a fixed count or going through a group; use `while` to repeat until something changes.

## 2.4 Functions
- `fun name(parameters): ReturnType { ... }`. Define once, call many times.
- Parameters are