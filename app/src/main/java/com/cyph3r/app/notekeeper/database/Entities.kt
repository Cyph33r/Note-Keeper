package com.cyph3r.app.notekeeper.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val NoteId: Int
)

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val CourseId: Int
)