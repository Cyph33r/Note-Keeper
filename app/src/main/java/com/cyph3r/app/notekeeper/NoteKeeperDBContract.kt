package com.cyph3r.app.notekeeper

import android.provider.BaseColumns
import android.provider.BaseColumns._ID


object NoteKeeperDBContract {

    object NoteEntry : BaseColumns {
        const val TABLE_NAME = "notes"
        const val COLUMN_NOTE_TITLE = "title"
        const val COLUMN_NOTE_BODY = "body"
        const val COLUMN_NOTE_COURSE = "course"
        const val COLUMN_DATE_CREATED = "date"

        const val SQL_CREATE_ENTRIES = """
            CREATE TABLE $TABLE_NAME(
            $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NOTE_TITLE  TEXT NOT NULL,
            $COLUMN_NOTE_BODY TEXT NOT NULL,
            $COLUMN_NOTE_COURSE INTEGER NOT NULL, 
            $COLUMN_DATE_CREATED INTEGER NOT NULL);
            """
        const val SQL_DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
