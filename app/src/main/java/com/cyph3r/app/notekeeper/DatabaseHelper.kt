package com.cyph3r.app.notekeeper


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "notekeeper.db"
        const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(NoteKeeperDBContract.NoteEntry.SQL_CREATE_NOTE_TABLE)
        db?.execSQL(NoteKeeperDBContract.CourseEntry.SQL_CREATE_COURSE_TABLE)
        initializeCourses(db)
    }

    private fun initializeCourses(db: SQLiteDatabase?) {
        val courses = mutableMapOf<String, String>()
        courses["android_intents"] = "Android Programming with Intents"
        courses["android_async"] = "Android Async Programming and Services"
        courses["java_lang"] = "Java Fundamentals in Java Language"
        courses["java_core"] ="Java Fundamental: The Core Platform"
        courses.forEach {
            val record = ContentValues()
            record.put(NoteKeeperDBContract.CourseEntry.COLUMN_COURSE_ID, it.key)
            record.put(NoteKeeperDBContract.CourseEntry.COLUMN_COURSE_NAME, it.value)
            val response = db?.insert(NoteKeeperDBContract.CourseEntry.TABLE_NAME, null, record)
            (context as NoteListActivity).showMessage("DataBase Response is $response")

        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(NoteKeeperDBContract.NoteEntry.SQL_DROP_NOTE_TABLE)
        db?.execSQL(NoteKeeperDBContract.CourseEntry.SQL_DROP_COURSE_TABLE)
        onCreate(db)
    }
}