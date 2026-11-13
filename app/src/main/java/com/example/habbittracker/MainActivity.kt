package com.example.habbittracker

import android.content.Intent
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private val habits = mutableListOf<Habit>()
    private lateinit var addHabitButton: Button
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
        addHabitButton = findViewById<Button>(R.id.addHabitButton)
        messageText = findViewById<TextView>(R.id.messageText)

        addHabitButton.setOnClickListener {
            startActivity(Intent(this, AddEditHabitActivity::class.java))
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

            val checkBox = CheckBox(this)
            checkBox.text = habit.name
            checkBox.textSize = 18f
            checkBox.setTextColor(ContextCompat.getColor(this,R.color.ink))
            checkBox.isChecked = habit.isCompleted
            checkBox.setPadding(0,12,0,12)
            checkBox.layoutParams = LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1f)

            checkBox.setOnClickListener{
                habits[index] = habit.copy(
                    isCompleted = checkBox.isChecked,
                    completedDate = if(checkBox.isChecked) todayString() else ""
                )
                storage.save(habits)
                showHabits()
            }

            val deleteButton = Button(this)
            deleteButton.text = "Delete"
            deleteButton.setOnClickListener{
//                habits.removeAt(index)
//                storage.save(habits)
//                messageText.text = "Deleted: ${habit.name}"
//                showHabits()
                AlertDialog.Builder(this)
                    .setTitle(R.string.delete_title)
                    .setMessage(getString(R.string.delete_message, habit.name))
                    .setPositiveButton(R.string.delete){_,_ ->
                        habits.removeAt(index)
                        storage.save(habits)
//                        messageText.text = "Deleted: ${habit.name}"
                        Toast.makeText(this,"Deleted: ${habit.name}", Toast.LENGTH_SHORT).show()
                        showHabits()
                    }
                    .setNegativeButton(R.string.cancel,null)
                    .show()
            }

            val editButton = Button(this)
            editButton.text = "Edit"
            editButton.setOnClickListener{
                val intent = Intent(this, AddEditHabitActivity::class.java)
                intent.putExtra(AddEditHabitActivity.EXTRA_HABIT_INDEX, index)
                startActivity(intent)
            }

            row.addView(editButton)
            row.addView(checkBox)
            row.addView(deleteButton)
            container.addView(row)
        }
    }
}