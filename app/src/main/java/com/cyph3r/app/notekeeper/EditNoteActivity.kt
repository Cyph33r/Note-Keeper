package com.cyph3r.app.notekeeper

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.cyph3r.app.notekeeper.database.*
import com.cyph3r.app.notekeeper.databinding.ActivityEditNoteBinding


class EditNoteActivity : AppCompatActivity() {
    private var noteId = EXTRA_NO_NOTE_ID
    private var logTag = this::class.simpleName
    private lateinit var activityEditNoteBinding: ActivityEditNoteBinding
    private lateinit var database: AppDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var courseDao: CourseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditNoteBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(activityEditNoteBinding.root)
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database.db")
            .allowMainThreadQueries().build()
        noteDao = database.noteDao()
        courseDao = database.courseDao()
        val adapterCourses = ArrayAdapter<Course>(
            this,
            android.R.layout.simple_spinner_item,
            courseDao.getAllCourses()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activityEditNoteBinding.spinnerCourses.adapter = adapterCourses


        noteId = savedInstanceState?.getInt(NOTE_ID) ?: intent.getIntExtra(
            NOTE_ID,
            EXTRA_NO_NOTE_ID
        )
        if (noteId != EXTRA_NO_NOTE_ID)
            displayNote()
        else {
            noteId = noteDao.addNote(
                NoteInsertEntry(
                    "",
                    "",
                    (activityEditNoteBinding.spinnerCourses.adapter.getItem(0) as Course).courseID,
                    System.currentTimeMillis()
                )
            ).toInt()
        }
        this.showMessage(noteId.toString())
        Log.d(logTag, "$logTag has been created")

    }

    private fun showMessage(toString: String) {
        Toast.makeText(this, toString, Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        Log.d(logTag, "$logTag has been paused")
        saveNote()

    }

    private fun saveNote() {
        val noteTitle = activityEditNoteBinding.fieldNoteTitle.text.trim().toString()
        val noteText = activityEditNoteBinding.fieldNoteText.text.toString().trim()
        val noteCourse = activityEditNoteBinding.spinnerCourses.selectedItem as Course
//        val dateCreated = System.currentTimeMillis()
        noteDao.updateNote(NoteUpdateEntry(noteId, noteTitle, noteText, noteCourse.courseID))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_ID, noteId)
        saveNote()
    }

    private fun displayNote() {
        val selectedNote = noteDao.getNoteById(noteId).first()
        val noteCourse = courseDao.findCourseById(selectedNote.noteCourseId)
        activityEditNoteBinding.fieldNoteTitle.setText(selectedNote.noteTitle)
        activityEditNoteBinding.fieldNoteText.setText(selectedNote.noteText)
        activityEditNoteBinding.spinnerCourses.setSelection(
            retrieveSpinnerItems().indexOf(noteCourse), true
        )
        invalidateOptionsMenu()

    }

    private fun retrieveSpinnerItems(): List<Course> {
        val adapter = activityEditNoteBinding.spinnerCourses.adapter
        val count = adapter.count
        val toReturn = ArrayList<Course>()
        for (i in 0 until count)
            toReturn.add(adapter.getItem(i) as Course)
        return toReturn

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        var menuItem: MenuItem?
        if (noteId >= noteDao.getSize() || noteId < 0) {
            menuItem = menu?.findItem(R.id.action_next)
            menuItem?.isVisible = false
            menuItem?.isEnabled = false
//            menuItem?.setIcon(R.drawable.ic_arrow_forward_grey_24dp)
        }
        if (noteId <= 1) {
            menuItem = menu?.findItem(R.id.action_back)
            menuItem?.isVisible = false
            menuItem?.isEnabled = false
//            menuItem?.setIcon(R.drawable.ic_arrow_back_grey_24dp)
        }
        return true
    }

    override fun onDestroy() {
        Log.d(logTag, "$logTag has been destroyed")
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                saveNote()
                ++noteId
                displayNote()
                true
            }
            R.id.action_back -> {
                saveNote()
                --noteId
                displayNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
