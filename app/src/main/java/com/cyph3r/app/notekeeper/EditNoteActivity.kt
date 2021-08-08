package com.cyph3r.app.notekeeper

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_note.*


private var position = EXTRA_NO_NOTE_POSITION

class EditNoteActivity : AppCompatActivity() {
    private var logTag = this::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
//        setSupportActionBar(toolbar_edit_note)
        val adapterCourses = ArrayAdapter<CourseInfo>(
            this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList()
        )
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_courses.adapter = adapterCourses


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

        Log.i(logTag, "$logTag has been created")

    }

    override fun onPause() {
        super.onPause()
        saveNote()

    }

    private fun saveNote() {
        val selectedNote = DataManager.notes[position]
        selectedNote.title = field_note_title.text.trim().toString()
        selectedNote.text = field_note_text.text.toString().trim()
        selectedNote.course = spinner_courses.selectedItem as CourseInfo
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, position)
        saveNote()
    }

    private fun displayNote() {
        val selectedNote = DataManager.notes[position]
        field_note_title.setText(selectedNote.title)
        field_note_text.setText(selectedNote.text)
        spinner_courses.setSelection(DataManager.courses.values.indexOf(selectedNote.course), true)
        invalidateOptionsMenu()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        var menuitem: MenuItem?
        if (position >= DataManager.notes.size - 1 || position < 0) {
            menuitem = menu?.findItem(R.id.action_next)
            menuitem?.isEnabled = false
            menuitem?.setIcon(R.drawable.ic_arrow_forward_grey_24dp)
        }
        if (position <= 0) {
            menuitem = menu?.findItem(R.id.action_back)
            menuitem?.isEnabled = false
            menuitem?.setIcon(R.drawable.ic_arrow_back_grey_24dp)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun invalidateOptionsMenu() {
        super.invalidateOptionsMenu()
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
