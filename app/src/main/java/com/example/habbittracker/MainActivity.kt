package com.example.habbittracker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val habits = mutableListOf<Habit>()
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

//        val textView = findViewById<TextView>(R.id.greetingText)
//        textView.setOnClickListener{
//            count++;
//            textView.text = "Taps: $count"
//        }

        habits.add(Habit("Drink water"))
        habits.add(Habit("Read 10 pages"))
        habits.add(Habit("Walk 15 minutes",true))
        habits.add(Habit("Sleep 8 hours",true))
        Log.d("HabitDebug","Habits in the list: ${habits.size}")
        showHabits()

        val habitNameInput = findViewById<EditText>(R.id.habitNameInput)
        val addHabitButton = findViewById<Button>(R.id.addHabitButton)
        val messageText = findViewById<TextView>(R.id.messageText)

        addHabitButton.setOnClickListener {
            val name = habitNameInput.text.toString().trim()
            if(name.isBlank()){
                messageText.text = "Habit name cannot be blank"
            }
            else if(habits.any{it.name.equals(name,ignoreCase = true)}){
                messageText.text = "This habit already exists!"
            }
            else if(name.length<3){
                messageText.text = "Habit name is too short"
            }
            else{
                habits.add(Habit(name))
                showHabits()
                messageText.text = "Added: $name"
                Log.d("HabitDebug","Habit added: $name, total: ${habits.size}")
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

        for(habit in habits){
            val row = TextView(this)
            row.text = if(habit.isCompleted) "✔ ${habit.name}" else "○ ${habit.name}"
            row.textSize = 18f
            row.setPadding(0,12,0,12)
            container.addView(row)
        }
    }
}