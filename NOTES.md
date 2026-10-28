# Week 1 Notes: Project Structure

## MainActivity.kt
- Holds the Kotlin logic for the first screen (an Activity = one screen).
- `onCreate()` runs when the screen is created. Android calls it automatically.
- `setContentView(R.layout.activity_main)` connects this screen to its XML layout.
- The edge-to-edge and insets lines come from the template. They add padding so content doesn't hide under the status and navigation bars.

## activity_main.xml
- Defines the screen's UI (what the user sees) in XML.
- Root is a `ConstraintLayout` (id `main`) that fills the screen and holds other views.
- Each view needs `layout_width` and `layout_height`.
- The `TextView` is centered by constraining all four sides to the parent.
- Use `sp` for text size, `dp` for other sizes.

## strings.xml
- Stores the app's text in one place (`res/values`).
- Referenced in layouts as `@string/name` instead of hardcoding text.
- Makes text easy to reuse, change and translate.
- Tested it with `greeting` and it showed "Good Morning!".

## AndroidManifest.xml
- The app's declaration file. Android reads it to learn what the app contains.
- Every Activity must be registered here, or it can't open.
- The `intent-filter` with `MAIN` + `LAUNCHER` marks MainActivity as the screen that opens when the app icon is tapped.
- `android:exported="true"` lets the launcher (another app) start this Activity.

## build.gradle.kts (Module :app)
- Build settings for the app, read during Gradle sync.
- `namespace` / `applicationId`: the app's package name.
- `minSdk`, `targetSdk`, `compileSdk`: the Android versions supported and built against.
- `dependencies { }`: the libraries the app uses. Room and Retrofit will be added here later.

## How the files connect
`AndroidManifest.xml` launches `MainActivity.kt` → `setContentView` loads `activity_main.xml` → the layout reads text from `strings.xml`.

## Git basics
- `git init`: create a repository
- `git status`: see what changed
- `git add .`: stage changes for the next commit
- `git commit -m "message"`: save a snapshot
- `git log --oneline`: see history
- `.gitignore` keeps build files, local settings and secrets out of Git

## Logcat
- `Log.d("tag","message")`: writes a debug message
- `Log.e("tag","message")`: writes an error message
- `Log.i("tag","message")`: writes an info message
- `Log.v("tag","message")`: writes a verbose message
- `Log.w("tag","message")`: writes a warning message
- Logcat shows live messages from the device (system and app). Open it from the Logcat tool window and select the phone at the top.
- Filters go in the search box: `package:mine` (my app only), `tag:MainActivity` (my own tag), `level:debug` (Debug and above).
- The letter beside each line is its level: V (Verbose), D (Debug), I (Info), W (Warn), E (Error, red).

## Writing logs
- `Log.d("MainActivity", "onCreate() is called")` prints a Debug message. First value is the tag, second is the message.
- `import android.util.Log` tells Kotlin where the `Log` class lives. Without it: "Unresolved reference". Alt+Enter adds imports automatically.
- Place `Log.d` below `super.onCreate(...)`. Keep `super` first in every lifecycle function.

## Activity lifecycle
- Order: onCreate → onStart → onResume → (running) → onPause → onStop → onDestroy.
- Open app: onCreate, onStart, onResume.
- Home button: onPause, onStop. Reopening: onStart, onResume (no onCreate).
- Back button: onPause, onStop, onDestroy.
- Lifecycle functions are separate functions inside the class, after `onCreate`, not inside it.

## Rotation
- Rotating the phone destroys and recreates the Activity (onDestroy then onCreate).
- Plain variables inside the Activity are lost on rotation (e.g. a `count` goes back to 0).
- Fixes learned later: save data with Room and use ViewModel, which survives rotation.

## Resource symbols and the R class
- `@string/name`, `@color/name`, `@drawable/name`: use something that already exists.
- `@+id/name` (only for ids) creates a new id when defining a view. `@id/name` refers to an existing one.
- `id` is a resource type, not a file. Android generates the `R` class at build time, and `@+id/greetingText` adds `R.id.greetingText` to it. Never edit `R`.

## Connecting XML and Kotlin
- Give a view an id in XML, then find it in Kotlin: `findViewById<TextView>(R.id.greetingText)`.
- Call this after `setContentView`, because the layout must be loaded first.
- `setOnClickListener { ... }` runs code when the view is tapped.
- `textView.text = "Taps: $count"` changes the displayed text. `$count` inserts a variable into a string.
