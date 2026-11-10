package com.example.habbittracker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View

class MainActivity : AppCompatActivity() {
    private val habits = mutableListOf<Habit>()
    private var editingIndex: Int? = null
    private lateinit var habitNameInput: EditText
    private lateinit var addHabitButton: Button
    private lateinit var cancelButton: Button
    private lateinit var messageText: TextView
    private lateinit var storage: HabitStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate() is called")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        storage = HabitStorage(this)
        habits.addAll(storage.load())
        Log.d("HabitDebug","Habits in the list: ${habits.size}")

        habitNameInput = findViewById<EditText>(R.id.habitNameInput)
        addHabitButton = findViewById<Button>(R.id.addHabitButton)
        messageText = findViewById<TextView>(R.id.messageText)
        cancelButton = findViewById<Button>(R.id.cancelButton)
        showHabits()

        addHabitButton.setOnClickListener {
            val name = habitNameInput.text.toString().trim().replace(Regex("\\s+")," ")
            val editIndex = editingIndex

            if(name.isBlank()){
                messageText.text = "Habit name cannot be blank"
            }
            else if(habits.withIndex().any{(i,h) -> i!=editIndex && h.name.equals(name,ignoreCase = true)}){
                messageText.text = "This habit already exists!"
            }
            else if(name.length<3){
                messageText.text = "Habit name is too short"
            }
            else{
                if(editIndex == null) {
                    habits.add(Habit(name))
                    storage.save(habits)
                    showHabits()
                    messageText.text = "Added: $name"
                    Log.d("HabitDebug", "Habit added: $name, total: ${habits.size}")
                    habitNameInput.text.clear()
                }
                else{
                    habits[editIndex] = habits[editIndex].copy(name = name)
                    storage.save(habits)
                    showHabits()
                    messageText.text = "Edited: $name"
                    Log.d("HabitDebug", "Habit edited: $name, total: ${habits.size}")
                    exitEditMode()
                }
            }
        }

        cancelButton.setOnClickListener {
            exitEditMode()
            messageText.text = "Edit Cancelled"
        }
    }

    override fun onStart(){
        super.onStart()
        Log.d("MainActivity","onStart() is called")
    }
    override fun onResume(){
        super.onResume()
        Log.d("MainActivity","onResume() is called")
        habits.clear()
        habits.addAll(storage.load())
        showHabits()
    }
    override fun onPause(){
        super.onPause()
        Log.d("MainActivity","onPause() is called")
    }
    override fun onStop(){
        super.onStop()
        Log.d("MainActivity","onStop() is called")
    }
    override fun onDestroy(){
        super.onDestroy()
        Log.d("MainActivity","onDestroy() is called")
    }

    private fun showHabits(){
        val container = findViewById<LinearLayout>(R.id.habitListContainer)
        container.removeAllViews()
        if(habits.isEmpty()){
            val empty = TextView(this)
            empty.text = "No habits yet! Add your first one!"
            empty.setTextColor(ContextCompat.getColor(this,R.color.ink))
            container.addView(empty)
            return
        }

        for((index,habit) in habits.withIndex()){
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL

            val checkbox = CheckBox(this)
            checkbox.text = habit.name
            checkbox.textSize = 18f
            checkbox.setTextColor(ContextCompat.getColor(this,R.color.ink))
            checkbox.isChecked = habit.isCompleted
            checkbox.setPadding(0,12,0,12)
            checkbox.layoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1f)

            checkbox.setOnClickListener {
                habits[index] = habit.copy(isCompleted = checkbox.isChecked,
                    completedDate = if(checkbox.isChecked) todayString() else "")
                storage.save(habits)
                showHabits()
            }

            val deleteButton = Button(this)
            deleteButton.text = "Delete"
            deleteButton.setOnClickListener {
                habits.removeAt(index)
                storage.save(habits)
                exitEditMode()
                messageText.text = "Deleted: ${habit.name}"
                showHabits()
            }

            val editButton = Button(this)
            editButton.text = "Edit"
            editButton.setOnClickListener{
                habitNameInput.setText(habit.name)
                editingIndex = index
                addHabitButton.text = "Save"
                cancelButton.visibility = View.VISIBLE
            }

            row.addView(editButton)
            row.addView(checkbox)
            row.addView(deleteButton)
            container.addView(row)
        }
    }

    private fun exitEditMode(){
        editingIndex = null
        addHabitButton.setText(R.string.add_habit)
        cancelButton.visibility = View.GONE
        habitNameInput.text.clear()
    }

}