package com.cyph3r.app.notekeeper


import android.content.ContentValues
import android.provider.BaseColumns
import com.cyph3r.app.notekeeper.DataManager.courses
import com.cyph3r.app.notekeeper.NoteKeeperDBContract.NoteEntry
import com.cyph3r.app.notekeeper.NoteKeeperDBContract.CourseEntry

object DataManager {


    lateinit var database: DatabaseHelper
    val courses: HashMap<String, CourseInfo>
        get() {
            val toReturn: HashMap<String, CourseInfo> = HashMap()
            val columns = arrayOf(
                BaseColumns._ID,
                CourseEntry.COLUMN_COURSE_ID,
                CourseEntry.COLUMN_COURSE_NAME
            )
            val cursor = database.readableDatabase.query(
                CourseEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null,
                null
            )
            val idPos = cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_COURSE_ID)
            val namePos = cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_COURSE_NAME)
            while (cursor.moveToNext()) {
                toReturn[cursor.getString(idPos)] =
                    CourseInfo(cursor.getString(idPos), cursor.getString(namePos))
            }
            cursor.close()
            return toReturn
        }
    val notes: ArrayList<NoteInfo>
        get() {
            val toReturn = ArrayList<NoteInfo>()
            val db = database.readableDatabase
            val cursor = db.query(NoteEntry.TABLE_NAME, null, null, null, null, null, null)
            val titlePos = cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NOTE_TITLE)
            val bodyPos = cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NOTE_BODY)
            val datePos = cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_DATE_CREATED)
            val coursePos = cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NOTE_COURSE)
            while (cursor.moveToNext()) {
                val noteCourse = courses[(cursor.getString(coursePos))]
                val note = NoteInfo(
                    noteCourse,
                    cursor.getString(titlePos),
                    cursor.getString(bodyPos),
                    (cursor.getString(datePos)).toLong()
                )
                toReturn.add(note)
            }
            cursor.close()
            return toReturn

        }

    fun addNote(
        course: CourseInfo,
        noteTitle: String,
        noteText: String,
        dateCreated: Long = System.currentTimeMillis()
    ): Long {

        val db = database.writableDatabase
        val record = ContentValues()
        record.put(NoteEntry.COLUMN_NOTE_TITLE, noteTitle)
        record.put(NoteEntry.COLUMN_NOTE_BODY, noteText)
        record.put(NoteEntry.COLUMN_DATE_CREATED, dateCreated)
        record.put(NoteEntry.COLUMN_NOTE_COURSE, course.courseId)
        val toReturn = db.insert(NoteEntry.TABLE_NAME, null, record)
        db.close()
        return toReturn

    }

    fun updateNote(
        noteId: Long,
        course: CourseInfo? = null,
        noteTitle: String? = null,
        noteText: String? = null,
        dateCreated: Long? = null
    ) {
        val update = ContentValues()
        val updateClause = " ${BaseColumns._ID} = $noteId"
        if (noteTitle != null)
            update.put(NoteEntry.COLUMN_NOTE_TITLE, noteTitle)
        if (noteText != null)
            update.put(NoteEntry.COLUMN_NOTE_BODY, noteText)
        if (course != null)
            update.put(NoteEntry.COLUMN_NOTE_COURSE, course.courseId)
        update.put(NoteEntry.COLUMN_DATE_CREATED, dateCreated)
        val db = database.writableDatabase
        db.update(NoteEntry.TABLE_NAME, update, updateClause, null)
        db.close()


    }

    fun getNotesUnderCourse(course: CourseInfo): List<NoteInfo> {
        val toReturn = mutableListOf<NoteInfo>()
        for (note in notes)
            if (note.course == course)
                toReturn.add(note)

        return toReturn.toList()
    }

}