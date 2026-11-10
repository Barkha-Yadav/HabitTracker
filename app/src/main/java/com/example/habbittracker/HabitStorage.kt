package com.example.habbittracker

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException

class HabitStorage (context: Context){
    private val pref = context.getSharedPreferences("habit_pref",Context.MODE_PRIVATE)

    fun save(habits: List<Habit>){
        val array = JSONArray()
        for(habit in habits){
            val obj = JSONObject()
            obj.put("name",habit.name)
            obj.put("isCompleted", habit.isCompleted)
            obj.put("completedDate", habit.completedDate)
            array.put(obj)
        }
        pref.edit().putString("habits", array.toString()).apply()
    }

    fun load(): MutableList<Habit>{
        val result = mutableListOf<Habit>()
        val json = pref.getString("habits",null) ?: return result
        try{
            val array = JSONArray(json)
            for(i in 0 until array.length()){
                val obj = array.getJSONObject(i)
                val savedDate = obj.optString("completedDate","")
                val doneToday = obj.getBoolean("isCompleted") && savedDate == todayString()
                result.add(Habit(obj.getString("name"), doneToday, if(doneToday) savedDate else ""))
            }
        } catch(e: JSONException){
            Log.e("HabitStorage","Could not read saved habits", e)
        }
        return result
    }
}