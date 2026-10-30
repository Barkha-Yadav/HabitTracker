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
- The edge-to-edge and insets lines come from the template. They add padding so content doesn't hide under the status and navigation bars.

### activity_main.xml
- Defines the screen's UI (what the user sees) in XML.
- Root is a `ConstraintLayout` (id `main`) that fills the screen and holds other views.
- Each view needs `layout_width` and `layout_height`.
  - `match_parent`: as big as the parent.
  - `wrap_content`: only as big as the content.
- The `TextView` is centered by constraining all four sides to the parent.
- Use `sp` for text size, `dp` for other sizes.
- `xmlns:android`, `xmlns:app`, `xmlns:tools` are namespace prefixes: `android:` for standard attributes, `app:` for library attributes (such as constraints), `tools:` for preview-only values.
- Design view shows the real look; Blueprint view shows only the outlines.

### strings.xml
- Stores the app's text in one place (`res/values`).
- Referenced in layouts as `@string/name` instead of hardcoding text.
- Makes text easy to reuse, change and translate.
- Tested it with `greeting` ("Good Morning!") and `textSize="32sp"`.

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
- `git add .`: stage changes for the next commit.
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
- Update it whenever a feature is finished (move it from Planned to Done).
- Markdown basics: `#` heading, `-` bullet, backticks for code, `|` for tables.
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
- Parameters are inputs. Order matters when calling.
- `return` sends a value back; the return type goes after the brackets.
- Default values: `fun addHabit(name: String, done: Boolean = false)`.
- Named arguments: `addHabit(name = "Read", done = true)` (order no longer matters).
- Single-expression function: `fun add(a: Int, b: Int) = a + b`.
- A function with no return type returns nothing useful.
- `return` alone stops a function early (useful for validation).
- Functions are written outside `main` and called from `main`. The program starts at `fun main()`, not `class main`.

## 2.5 Lists
- `listOf(...)` is read-only; `mutableListOf(...)` can be changed with `add`, `remove`, `removeAt`, `clear`.
- Positions (indexes) start at 0: `list[0]` is the first item. `list.size` is the number of items.
- `val` on a mutable list means the variable can't point to another list, but the contents can change.
- Loop: `for (item in list) { ... }`; with position: `list.withIndex()`.
- `filter { condition }` keeps matching items; `map { change }` transforms every item. Both return a new list.
- `count { condition }` counts the items that match the condition.
- `isEmpty()` checks for an empty list (used for the empty state in the app).
- `it` only works inside `{ }` lambdas like `filter`, `map`, `count`, `removeAll`.
- Never remove items from a list while looping over it with `for` (it crashes). Use `removeAll { condition }` or `filter { condition }`.
- `removeAt` belongs to the list (`habits.removeAt(0)`), not to a single item.
- `List<T>` is read-only; use `MutableList<T>` when a function must change the list.

## 2.6 Classes and data classes
- A class groups related information into one type; an object is one real instance made from it.
- `data class Habit(val name: String, val isCompleted: Boolean = false)` auto-generates `toString()`, `equals()` and `copy()`.
- Read a property with a dot: `habit.name`.
- With `val` properties, use `copy()` to make a changed version: `habit.copy(isCompleted = true)`. The original stays unchanged.
- `==` on data classes compares contents, not memory identity.
- On a list of Habits, `filter { it.isCompleted }` keeps only the completed ones.
- Marking a habit complete in the app = replacing it with a copy where `isCompleted = true`.

## 2.7 Null safety
- A normal type (`String`) can never be null; a nullable type (`String?`) can be text or null.
- A nullable cannot be used directly (`note.length` is an error); handle the null case first.
- Safe call `?.` returns null instead of crashing: `note?.length`.
- Elvis `?:` gives a default when the left side is null: `note ?: "No note"`, `note?.length ?: 0`.
- `!!` crashes with NullPointerException if the value is null. Avoid it.
- `if (x != null) { ... }` lets me use `x` normally inside the block.

## 2.8 Exceptions
- An exception is an error at run time. If not handled, the app crashes (e.g. `"abc".toInt()` throws NumberFormatException).
- `try { risky code } catch (e: ExceptionType) { handle it }` stops the crash and the program continues.
- `toIntOrNull()` returns null instead of crashing when the text isn't a number. Combine with `?:` or `if (x == null)`.
- Prefer `toIntOrNull()` for number input; use `try/catch` when no safe version exists.
- Always validate user input and show a friendly message.

## 2.9 Mini exercise: habit list (console version)
Functions written: `printHabits`, `markCompleted`, `addHabit`, `deleteHabit`, `renameHabit`.
- Put repeated code in a function and call it instead of copying.
- `index !in list.indices` checks that a position is valid, so the app doesn't crash.
- Complete or rename by replacing with a copy: `habits[i] = habits[i].copy(name = newName.trim())`.
- Trim user text before saving, in every function that saves a name (add and rename).
- Code index starts at 0, but the numbered list shown to the user starts at 1 (`index + 1`).
- Test both the valid and the invalid case of every function (wrong index, blank name).
- This is the same add / complete / edit / delete logic the Habit Tracker app will use.

## 2.10 Interfaces, inheritance and companion object
Practised in https://play.kotlinlang.org with tiny examples.

### Interface
- An interface is a promise: it lists functions that a class must provide.
- A class says `: InterfaceName` and fills in each function with `override`.
- Example: `interface Describable { fun describe(): String }`, then `data class Habit(...) : Describable { override fun describe(): String { ... } }`.
- A function that returns a String does not show it. Use `println(habit.describe())`.

### Inheritance (open / override / super)
- Kotlin classes are closed by default. `open class` allows other classes to inherit from it.
- A function must also be `open` before a child class can change it.
- Child class: `class MorningReminder(title: String) : Reminder(title)`. The `: Reminder(title)` passes the value to the parent.
- `override fun message()` replaces the parent's version; `super.message()` calls the parent's version.
- A `data class` cannot be `open`, so inheritance examples use a normal class.
- Android link: `class MainActivity : AppCompatActivity()` is inheritance, and `override fun onCreate(...)` with `super.onCreate(...)` is the same pattern.

### Companion object
- `companion object { }` holds values and functions that belong to the class itself, not to one object.
- Call them without creating an object: `HabitRules.isValidName("Read")`.
- `const val MAX_NAME_LENGTH = 30` is a fixed value known at compile time (only for simple values like numbers and Strings).
- Android link: `companion object { private const val TAG = "MainActivity" }` is common for Log tags.
- Habit Tracker use: `isValidName` can hold the habit-name validation in one place.

## 2.11 Still only read, and recap
- Only read so far: delegation, object declarations (`object Name { }` = one shared instance, a singleton) and anonymous objects.
- Day 2 recap quiz: 5/5. Reminders: `when` needs `in` for ranges; `if` is an expression; `?.` plus `?:` handles null; `toIntOrNull()` is safe for input; `!in list.indices` checks a valid index.

---

# Part 3: Kotlin in Android

## 3.1 Habit.kt
- Moved the `Habit` data class into the Android project as its own file `Habit.kt`.
- Created with: right-click the package > New > Kotlin Class/File > File.
- Same package as MainActivity: `package com.example.habbittracker`.

```kotlin
package com.example.habbittracker

data class Habit(
    val name: String,
    val isCompleted: Boolean = false
)
```

- `val` fields never change; to change one, use `copy()`.
- The default `= false` means a new habit starts as not done.

## 3.2 Printing habits to Logcat
- Added a private function inside `MainActivity`, below `onCreate`, and called it from `onCreate` after `setContentView`.
- A separate function keeps `onCreate` clean.

```kotlin
private fun logSampleHabits() {
    val habits = listOf(
        Habit("Drink water"),
        Habit("Read 10 pages", true),
        Habit("Walk")
    )

    for ((index, habit) in habits.withIndex()) {
        Log.d("HabitDebug", "${index + 1}. ${habit.name} - done: ${habit.isCompleted}")
    }

    val doneCount = habits.count { it.isCompleted }
    Log.d("HabitDebug", "Completed $doneCount of ${habits.size}")
}
```

- Logcat filter: `tag:HabitDebug`.
- Expected output: three numbered habit lines, then "Completed 1 of 3".
- `withIndex()` gives the position and the item; `index + 1` shows a human-friendly number.
- `count { it.isCompleted }` counts completed habits.
- This is the first time my Kotlin practice runs inside a real Android project. In Week 2 the same list will appear on screen.

---

# Part 4: Common errors I met

| Error / mistake | Cause | Fix |
|---|---|---|
| `Incompatible types 'Int' and 'IntRange'` | Wrote `1..6 ->` in `when` | Write `in 1..6 ->` |
| `Unresolved reference 'removeAt' on receiver of type 'String'` | Called a list function on one item | Call it on the list: `habits.removeAt(i)` |
| `Unresolved reference 'it'` | Used `it` outside a `{ }` lambda | Use the loop variable name |
| Many "Expecting member declaration" errors | Wrote `public class main()` | Write `fun main()` |
| `'if' must have both main and 'else' branches` | Used `? :` like other languages | Use `if (cond) a else b` |
| Output stuck on one line | Used `print` instead of `println` | Use `println` |
| Warning "could be val" | `var` never changed | Use `val` |
| `Unresolved reference: Log` | Missing import | `import android.util.Log` (Alt+Enter) |
| No output in Kotlin Playground | Function returned a String but I never printed it | `println(habit.describe())` |

Tips:
- When there are many errors, fix the first one and run again. The rest often disappear.
- Before asking for help, note the error and what I already tried.

---

# Part 5: Week 1 review checklist
- [ ] Starter app builds and runs on my phone
- [ ] Git repository with meaningful commits, pushed to GitHub
- [ ] Notes on project structure and Kotlin exercises (this file)
- [ ] README.md on GitHub with run steps, features and known limitations
- [ ] Habit.kt added to the project and sample habits printed to Logcat
- [ ] I can explain: lifecycle order, why rotation loses data, `findViewById` + `setOnClickListener`
- [ ] I can explain: `val`/`var`, null safety, data class and `copy()`
- [ ] I can explain: interface vs inheritance (`open` / `override` / `super`) and what a companion object is
- [ ] Pending issues to discuss with mentor: (add here)