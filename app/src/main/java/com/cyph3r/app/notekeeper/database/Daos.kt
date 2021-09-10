package com.cyph3r.app.notekeeper.database

import androidx.room.*


@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE NoteId = :id")//todo:ensure this works
    fun getNoteById(id: Int): List<Note>

    @Delete
    fun deleteNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Note::class)
    fun addNote(note: NoteInsertEntry): Long

    @Update(entity = Note::class)
    fun updateNote(note: NoteUpdateEntry)

    @Query("SELECT * FROM notes WHERE noteCourseId = :courseId")//todo:could be a problem later
    fun getNotesByCourse(courseId: String): List<Note>

    @Query(" SELECT COUNT(*) FROM notes")
    fun getSize(): Int
}

@Dao
interface CourseDao {

    @Query("SELECT * FROM courses")
    fun getAllCourses(): List<Course>

    @Insert
    fun addCourse(course: Course)

    @Delete
    fun removeCourse(course: Course)

    @Query("SELECT * FROM  courses WHERE courseId = :courseId")
    fun findCourseById(courseId: String): Course

    @Query(" SELECT COUNT(*) FROM courses")
    fun getSize(): Int

}