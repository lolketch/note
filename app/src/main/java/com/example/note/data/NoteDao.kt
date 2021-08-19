package com.example.note.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.note.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note")
    fun all(): List<Note?>?

    @Query("SELECT * FROM Note")
    fun allLiveData(): LiveData<List<Note?>?>?

    @Query("SELECT * FROM Note WHERE uid IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray?): List<Note?>?

    @Query("SELECT * FROM Note WHERE uid = :notesid")
    suspend fun getNotes(notesid: String): Note

    @Query("UPDATE Note SET date_reminder = :date_reminder WHERE uid LIKE :uid ")
    fun updateReminderDate(uid: Long, date_reminder: Long)

    @Query("SELECT * FROM Note WHERE uid = :uid LIMIT 1")
    fun findById(uid: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note?):Long

    @Update
    fun update(note: Note?)

    @Delete
    fun delete(note: Note?)
}