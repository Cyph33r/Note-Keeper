package com.cyph3r.app.notekeeper.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Note::class, Course::class), version = 1)


abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun courseDao(): CourseDao
}