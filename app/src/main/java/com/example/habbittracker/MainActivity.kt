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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val habits = mutableListOf<Habit>()
    private var editingIndex: Int? = null
    private lateinit var habitNameInput: EditText
    private lateinit var addHabitButton: Button
    private lateinit var messageText: TextView
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

        habits.add(Habit("Drink water"))
        habits.add(Habit("Read 10 pages"))
        habits.add(Habit("Walk 15 minutes",true))
        habits.add(Habit("Sleep 8 hours",true))
        Log.d("HabitDebug","Habits in the list: ${habits.size}")

        habitNameInput = findViewById<EditText>(R.id.habitNameInput)
        addHabitButton = findViewById<Button>(R.id.addHabitButton)
        messageText = findViewById<TextView>(R.id.messageText)
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
                    showHabits()
                    messageText.text = "Added: $name"
                    Log.d("HabitDebug", "Habit added: $name, total: ${habits.size}")
                }
                else{
                    habits[editIndex] = habits[editIndex].copy(name = name)
                    showHabits()
                    messageText.text = "Edited: $name"
                    Log.d("HabitDebug", "Habit edited: $name, total: ${habits.size}")
                    editingIndex = null
                    addHabitButton.text = "Add"
                }
                habitNameInput.text.clear()
            }
        }
        logSampleHabits()
    }

    override fun onStart(){
        super.onStart()
        Log.d("MainActivity", "onStart is called")
    }
    override fun onResume(){
        super.onResume()
        Log.d("MainActivity","onResume is called")
    }
    override fun onPause(){
        super.onPause()
        Log.d("MainActivity","onPause is called")
    }
    override fun onStop(){
        super.onStop()
        Log.d("MainActivity","onStop is called")
    }
    override fun onDestroy(){
        super.onDestroy()
        Log.d("MainActivity","onDestroy is called")
    }

    private fun logSampleHabits(){
        val sampleHabits = listOf(
            Habit("Drink Water"),
            Habit("Read 10 pages",true),
            Habit("Walk"),
            Habit("Meditate",true)
        )
        for((index,habit) in sampleHabits.withIndex()){
            Log.d("HabitDebug","${index+1}. ${habit.name} - done: ${habit.isCompleted}")
        }

        val doneCount = sampleHabits.count{it.isCompleted}
        Log.d("HabitDebug","Completed $doneCount of ${sampleHabits.size}")

        val notCompletedHabits = sampleHabits.filter{!it.isCompleted}
        Log.d("HabitDebug","Not completed habits: $notCompletedHabits")
    }

    private fun showHabits(){
        val container = findViewById<LinearLayout>(R.id.habitListContainer)
        container.removeAllViews()
        if(habits.isEmpty()){
            val empty = TextView(this)
            empty.text = "No habits yet! Add your first one!"
            container.addView(empty)
            return
        }

        for((index,habit) in habits.withIndex()){
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL

            val checkbox = CheckBox(this)
            checkbox.text = habit.name
            checkbox.textSize = 18f
            checkbox.isChecked = habit.isCompleted
            checkbox.setPadding(0,12,0,12)
            checkbox.layoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1f)

            checkbox.setOnClickListener {
                habits[index] = habit.copy(isCompleted = checkbox.isChecked)
                showHabits()
            }

            val deleteButton = Button(this)
            deleteButton.text = "Delete"
            deleteButton.setOnClickListener {
                habits.removeAt(index)
                editingIndex = null
                addHabitButton.text = "Add"
                messageText.text = "Deleted: ${habit.name}"
                habitNameInput.text.clear()
                showHabits()
            }

            val editButton = Button(this)
            editButton.text = "Edit"
            editButton.setOnClickListener{
                habitNameInput.setText(habit.name)
                editingIndex = index
                addHabitButton.text = "Save"
            }

            row.addView(editButton)
            row.addView(checkbox)
            row.addView(deleteButton)
            container.addView(row)
        }
    }

}