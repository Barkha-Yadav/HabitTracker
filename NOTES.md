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

## Kotlin exercises
(add later this week)