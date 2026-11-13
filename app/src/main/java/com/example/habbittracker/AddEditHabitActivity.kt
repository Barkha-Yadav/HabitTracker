package com.example.habbittracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddEditHabitActivity : AppCompatActivity() {
    private lateinit var storage: HabitStorage
    private lateinit var habits: MutableList<Habit>
    private lateinit var habitNameInput: EditText
    private var editIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit_habit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        storage = HabitStorage(this)
        habits = storage.load()

        habitNameInput = findViewById(R.id.habitNameInput)
        val formTitle = findViewById<TextView>(R.id.formTitle)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        editIndex = intent.getIntExtra(EXTRA_HABIT_INDEX, -1)
        if(editIndex in habits.indices){
            formTitle.setText(R.string.edit_habit)
            habitNameInput.setText(habits[editIndex].name)
        } else{
            editIndex = -1
        }

        saveButton.setOnClickListener{ saveHabit()}
        cancelButton.setOnClickListener{ finish()}
    }

    private fun saveHabit(){
        val name = habitNameInput.text.toString().trim().replace(Regex("\\s+")," ")

        when{
            name.isBlank() -> habitNameInput.error = "Habit name can not be blank!"
            name.length<3 -> habitNameInput.error = "Habit name is too short!"
            habits.withIndex().any{(i,h) -> i!=editIndex && h.name.equals(name,ignoreCase = true)} -> habitNameInput.error = "This habit already exists!"
            else -> {
                if(editIndex == -1){
                    habits.add(Habit(name))
                    Toast.makeText(this,"Added: $name",Toast.LENGTH_SHORT).show()
                }
                else{
                    habits[editIndex] = habits[editIndex].copy(name=name)
                    Toast.makeText(this,"Updated: $name",Toast.LENGTH_SHORT).show()
                }
                storage.save(habits)
                finish()
            }
        }
    }
    companion object{
        const val EXTRA_HABIT_INDEX = "habit_index"
    }
}