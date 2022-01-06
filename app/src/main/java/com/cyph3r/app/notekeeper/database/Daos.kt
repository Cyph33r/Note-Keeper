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

    @Query("DELETE FROM notes WHERE NoteId = :noteId")
    fun deleteNoteById(noteId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Note::class)
    fun addNote(note: NoteInsertEntry): Long

    @Update(entity = Note::class)
    fun updateNote(note: NoteUpdateEntry)

    @Query("SELECT * FROM notes WHERE noteCourseId = :courseId")//todo:could be a problem later
    fun getNotesByCourse(courseId: String): List<Note>

    @Query(" SELECT COUNT(*) FROM notes")
    fun getSize(): Int

    @Query("SELECT * FROM notes WHERE noteTitle LIKE :keyword")
    fun searchNoteByTitle(keyword: String):List<Note>

    @Query("SELECT * FROM notes WHERE noteText LIKE :keyword")
    fun searchNoteByText(keyword: String):List<Note>
}

@Dao
interface CourseDao {

    @Query("SELECT * FROM courses")
    fun getAllCourses(): List<Course>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Course::class)
    fun addCourse(course: Course)

    @Delete
    fun removeCourse(course: Course)

    @Query("SELECT * FROM  courses WHERE courseID = :courseID")
    fun findCourseById(courseID: String): Course

    @Query(" SELECT COUNT(*) FROM courses")
    fun getSize(): Int

}