package com.devdtoo.keepnotes.db

import androidx.room.*

@Dao
interface NoteDao {


//    suspend is used for call the function inside a coroutines scope...so we cant directly call a fun that has suspend...we need coroutine or it

//    To Insert a note

    @Insert
    suspend fun addNote(note : Note)


//    To get all notes from DB

    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes() : List<Note>


//    To Insert Multiple Notes at a time

    @Insert
    suspend fun addMultipleNotes(vararg note: Note)


//  this is to update an existing note
    @Update
    suspend fun updateNote(note: Note)

//    this is to delete a note
    @Delete
    suspend fun deleteNote(note: Note);

}