package com.devdtoo.keepnotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase(){

    abstract fun getNoteDao() : NoteDao

    companion object {

        @Volatile private var instance : NoteDatabase? = null

        private val LOCK = Any()


//        if the instance is not null, return the instance
//        if the instance is null...it builds database n initialise the instance n than returns it
        operator fun invoke(context: Context ) = instance?: synchronized(LOCK) {
            instance?: buildDatabase(context).also {
                instance = it
            }
        }
//      Builds Database
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "notedatabase"
        ).build()


    }

}