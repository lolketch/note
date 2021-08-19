package com.example.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.note.model.Note

class MainViewModel : ViewModel() {
    val noteLiveData: LiveData<List<Note?>?>? = App.instance?.noteDao?.allLiveData()
}