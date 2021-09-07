package com.cyph3r.app.notekeeper

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cyph3r.app.notekeeper.databinding.ActivityEditNoteBinding


class EditNoteActivity : AppCompatActivity() {
    private var position = EXTRA_NO_NOTE_POSITION
    private var logTag = this::class.simpleName
    private lateinit var activityEditNoteBinding: ActivityEditNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditNoteBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(activityEditNoteBinding.root)
        val adapterCourses = ArrayAdapter<CourseInfo>(
            this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        activityEditNoteBinding.spinnerCourses.adapter = adapterCourses


        position = savedInstanceState?.getLong(NOTE_POSITION) ?: intent.getLongExtra(
            NOTE_POSITION,
            EXTRA_NO_NOTE_POSITION
        )
        if (position != EXTRA_NO_NOTE_POSITION)
            displayNote()
        else {
            position = DataManager.addNote(
                activityEditNoteBinding.spinnerCourses.selectedItem as CourseInfo,
                "",
                ""
            )
        }
        this.showMessage(position.toString())
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
        val noteCourse = activityEditNoteBinding.spinnerCourses.selectedItem as CourseInfo
        val dateCreated = System.currentTimeMillis()
        DataManager.updateNote(position + 1, noteCourse, noteTitle, noteText, dateCreated)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(NOTE_POSITION, position)
        saveNote()
    }

    private fun displayNote() {
        val selectedNote = DataManager.notes[position.toInt()]
        activityEditNoteBinding.fieldNoteTitle.setText(selectedNote.title)
        activityEditNoteBinding.fieldNoteText.setText(selectedNote.text)
        activityEditNoteBinding.spinnerCourses.setSelection(
            DataManager.courses.values.indexOf(
                selectedNote.course
            ), true
        )
        invalidateOptionsMenu()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        var menuItem: MenuItem?
        if (position >= DataManager.notes.size || position < 0) {
            menuItem = menu?.findItem(R.id.action_next)
            menuItem?.isVisible = false
            menuItem?.isEnabled = false
//            menuItem?.setIcon(R.drawable.ic_arrow_forward_grey_24dp)
        }
        if (position <= 1) {
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
                ++position
                displayNote()
                true
            }
            R.id.action_back -> {
                saveNote()
                --position
                displayNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
