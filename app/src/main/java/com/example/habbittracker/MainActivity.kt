package com.example.habbittracker

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var count = 0 //  lives inside this activity only

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

        val textView = findViewById<TextView>(R.id.greetingText)
        textView.setOnClickListener{
            count++;
            textView.text = "Taps: $count"
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
        val habits = listOf(
            Habit("Drink Water"),
            Habit("Read 10 pages",true),
            Habit("Walk"),
            Habit("Meditate",true)
        )
        for((index,habit) in habits.withIndex()){
            Log.d("HabitDebug","${index+1}. ${habit.name} - done: ${habit.isCompleted}")
        }

        val doneCount = habits.count{it.isCompleted}
        Log.d("HabitDebug","Completed $doneCount of ${habits.size}")

        val notCompletedHabits = habits.filter{!it.isCompleted}
        Log.d("HabitDebug","Not completed habits: $notCompletedHabits")
    }
}