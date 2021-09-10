package com.cyph3r.app.notekeeper.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val NoteId: Int,
    @ColumnInfo val noteTitle: String,
    @ColumnInfo val noteText: String,
    @ColumnInfo val noteCourseId: String,
    @ColumnInfo val dateCreated: Long

)

data class NoteInsertEntry(
    val noteTitle: String,
     val noteText: String,
     val noteCourseId: String,
     val dateCreated: Long
)
data class NoteUpdateEntry(
    val NoteId: Long,
    val noteTitle: String,
    val noteText: String,
    val noteCourseId: String//todo:update dateCreated too if note contents are changed
)


@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val courseId: String,
    @ColumnInfo val courseName: String
)








