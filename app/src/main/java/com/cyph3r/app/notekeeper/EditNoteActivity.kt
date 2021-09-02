package com.cyph3r.app.notekeeper

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.cyph3r.app.notekeeper.databinding.ActivityEditNoteBinding


private var position = EXTRA_NO_NOTE_POSITION

class EditNoteActivity : AppCompatActivity() {
    private var logTag = this::class.simpleName
    lateinit var activityEditNoteBinding: ActivityEditNoteBinding
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


        position = savedInstanceState?.getInt(NOTE_POSITION) ?: intent.getIntExtra(
            NOTE_POSITION,
            EXTRA_NO_NOTE_POSITION
        )
        if (position != EXTRA_NO_NOTE_POSITION)
            displayNote()
        else {
            DataManager.notes.add(NoteInfo())
            position = DataManager.notes.lastIndex
        }

        Log.d(logTag, "$logTag has been created")

    }

    override fun onPause() {
        super.onPause()
        Log.d(logTag, "$logTag has been paused")
        saveNote()

    }

    private fun saveNote() {
        val selectedNote = DataManager.notes[position]
        selectedNote.title = activityEditNoteBinding.fieldNoteTitle.text.trim().toString()
        selectedNote.text = activityEditNoteBinding.fieldNoteText.text.toString().trim()
        selectedNote.course = activityEditNoteBinding.spinnerCourses.selectedItem as CourseInfo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, position)
        saveNote()
    }

    private fun displayNote() {
        val selectedNote = DataManager.notes[position]
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
        if (position >= DataManager.notes.size - 1 || position < 0) {
            menuItem = menu?.findItem(R.id.action_next)
            menuItem?.isEnabled = false
            menuItem?.setIcon(R.drawable.ic_arrow_forward_grey_24dp)
        }
        if (position <= 0) {
            menuItem = menu?.findItem(R.id.action_back)
            menuItem?.isEnabled = false
            menuItem?.setIcon(R.drawable.ic_arrow_back_grey_24dp)
        }
        return true
    }

    override fun onDestroy() {
        Log.d(logTag, "$logTag has been destroyed")
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
