package com.example.note

import android.app.Application
import androidx.room.Room
import com.example.note.data.AppDatabase
import com.example.note.data.NoteDao

class App:Application() {
    lateinit var database: AppDatabase
    lateinit var noteDao: NoteDao

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder<AppDatabase>(
            applicationContext,
            AppDatabase::class.java, "app-db-name")
            .allowMainThreadQueries()
            .build()
        noteDao = database.noteDao()!!
    }

    companion object {
        var instance: App? = null
            private set
    }
}