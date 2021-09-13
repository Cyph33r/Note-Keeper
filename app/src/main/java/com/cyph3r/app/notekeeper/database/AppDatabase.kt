package com.cyph3r.app.notekeeper.database

import androidx.room.Database
import androidx.room.RoomDatabase




@Database(entities = [Note::class, Course::class], version = 1, exportSchema = true)

abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun courseDao(): CourseDao
}