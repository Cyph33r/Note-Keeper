package com.cyph3r.app.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_list_note.*
import kotlinx.android.synthetic.main.drawer.*


class NoteListActivity : AppCompatActivity(), OnNavigationItemSelectedListener {

    private val noteRecyclerAdapter by lazy { NoteRecyclerAdapter(this, DataManager.notes) }
    private val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(
            this,
            DataManager.courses.values.toList()
        )
    }
    private val noteLayout by lazy { LinearLayoutManager(this) }
    private val courseLayout by lazy { GridLayoutManager(this, 2) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer)
        setSupportActionBar(toolbar_notes_list)

        add_note_button.setOnClickListener { _ ->
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, EXTRA_NO_NOTE_POSITION)
            startActivity(activityIntent)
        }
        displayNotes()
        val toggle = ActionBarDrawerToggle(
            this,
            activity_main_drawer,
            toolbar_notes_list,
            R.string.open_drawer,
            R.string.close_drawer
        )
        activity_main_drawer.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun displayNotes() {
        note_course_list.layoutManager = noteLayout
        note_course_list.adapter = noteRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        note_course_list.layoutManager = courseLayout
        note_course_list.adapter = courseRecyclerAdapter
        nav_view.menu.findItem(R.id.nav_courses).isChecked = true
    }

    override fun onBackPressed() {
        if (activity_main_drawer.isDrawerOpen(GravityCompat.START))
            activity_main_drawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        note_course_list.adapter?.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_notes -> displayNotes()
            R.id.nav_courses -> displayCourses()
            R.id.nav_share -> showMessage("You pressed share")
            R.id.nav_send -> showMessage("You pressed send")
        }
        activity_main_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun showMessage(message: String) {
        Snackbar.make(note_course_list, message, Snackbar.LENGTH_SHORT).show()
    }


}
