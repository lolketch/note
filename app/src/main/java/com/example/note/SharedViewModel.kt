package com.example.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SharedViewModel:ViewModel() {
    private var _data = MutableLiveData("")
    private var _reminder = MutableLiveData("Установить дату")
    private var _calendar = MutableLiveData<Calendar>()
    val data: LiveData<String> = _data
    val reminder: MutableLiveData<String> = _reminder
    val save_calendar: LiveData<Calendar> = _calendar

    fun saveTitle(newCountry:String){
        _data.value = newCountry
    }
    fun saveDate(dateReminder: String){
        _reminder.value = dateReminder
    }
    fun saveCalendar(save_calendar:Calendar){
        _calendar.value = save_calendar
    }
    fun clearTitle(){
        _data.value = ""
    }
    fun clearDate(){
        _reminder.value = "Установить дату"
    }

}