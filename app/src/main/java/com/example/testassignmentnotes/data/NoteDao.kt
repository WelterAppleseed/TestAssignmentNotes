package com.example.testassignmentnotes.data

import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteDbo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(vararg note: NoteDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteList(notes: List<NoteDbo>)

    @Delete
    fun remove(vararg note: NoteDbo)

}