package com.example.habbittracker

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun todayString(): String{
    return SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
}