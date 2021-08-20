package com.example.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class SharedViewModel:ViewModel() {
    private var _data = MutableLiveData("")
    private var _reminderDate = MutableLiveData("")
    private var _reminderTime = MutableLiveData("")
    private var _calendar = MutableLiveData<Calendar>()

    private var _nameContact = MutableLiveData("")
    private var _emailContact = MutableLiveData("")
    private var _imageContact = MutableLiveData(R.drawable.default_photo.toString())

    val nameContact: LiveData<String> = _nameContact
    val emailContact: LiveData<String> = _emailContact
    val imageContact: LiveData<String> = _imageContact


    val data: LiveData<String> = _data
    val reminderDate: MutableLiveData<String> = _reminderDate
    val reminderTime: MutableLiveData<String> = _reminderTime
    val save_calendar: LiveData<Calendar> = _calendar

    fun saveTitle(newCountry:String){
        _data.value = newCountry
    }
    fun saveDate(dateReminder: String){
        _reminderDate.value = dateReminder
    }
    fun saveTime(timeReminder:String){
        _reminderTime.value = timeReminder
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
        _reminderDate.value = ""
        _reminderTime.value = ""
    }
    fun clearContact(){
        _nameContact.value = ""
        _emailContact.value = ""
        _imageContact.value = R.drawable.default_photo.toString()
    }

}