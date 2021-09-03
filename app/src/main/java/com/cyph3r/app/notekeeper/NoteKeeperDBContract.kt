package com.cyph3r.app.notekeeper

import android.provider.BaseColumns
import android.provider.BaseColumns._ID


object NoteKeeperDBContract {

    const val SQL_INITIALIZE_COURSES_ = """
        INSERT INTO
    """

    object CourseEntry : BaseColumns {
        const val TABLE_NAME = "courses"
        const val COLUMN_COURSE_NAME = "course_name"
        const val COLUMN_COURSE_ID = "course_name_id"
        const val SQL_CREATE_COURSE_TABLE = """
            
            CREATE TABLE $TABLE_NAME(
            $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_COURSE_ID STRING NOT NULL,
            $COLUMN_COURSE_NAME STRING NOT NULL
            );
        """

        const val SQL_DROP_COURSE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    object NoteEntry : BaseColumns {
        const val TABLE_NAME = "notes"
        const val COLUMN_NOTE_TITLE = "title"
        const val COLUMN_NOTE_BODY = "body"
        const val COLUMN_NOTE_COURSE = "course"
        const val COLUMN_DATE_CREATED = "date"

        const val SQL_CREATE_NOTE_TABLE = """
            CREATE TABLE $TABLE_NAME(
            $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NOTE_TITLE  TEXT NOT NULL,
            $COLUMN_NOTE_BODY TEXT NOT NULL,
            $COLUMN_NOTE_COURSE INTEGER NOT NULL, 
            $COLUMN_DATE_CREATED INTEGER NOT NULL);
            """
        const val SQL_DROP_NOTE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
