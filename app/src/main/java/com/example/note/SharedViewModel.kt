package com.example.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class SharedViewModel:ViewModel() {
    private var _data = MutableLiveData("")
    private var _reminder = MutableLiveData("Установить дату")
    private var _calendar = MutableLiveData<Calendar>()

    private var _nameContact = MutableLiveData("")
    private var _emailContact = MutableLiveData("")
    private var _imageContact = MutableLiveData(R.drawable.default_photo.toString())

    val nameContact: LiveData<String> = _nameContact
    val emailContact: LiveData<String> = _emailContact
    val imageContact: LiveData<String> = _imageContact


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
    fun saveNameContact(nameContact:String){
        println("SharedViewModel " + nameContact)
        _nameContact.value = nameContact
    }
    fun saveEmailContact(emailContact:String){
        _emailContact.value = emailContact
    }
    fun saveImageContact(imageContact:String){
        _imageContact.value = imageContact
    }
    fun clearTitle(){
        _data.value = ""
    }
    fun clearDate(){
        _reminder.value = "Установить дату"
    }
    fun clearContact(){
        _nameContact.value = ""
        _emailContact.value = ""
        _imageContact.value = R.drawable.default_photo.toString()
    }

}