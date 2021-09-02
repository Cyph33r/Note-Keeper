package com.cyph3r.app.notekeeper

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyph3r.app.notekeeper.databinding.ActivityListNoteBinding
import com.cyph3r.app.notekeeper.databinding.DrawerBinding
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar


class NoteListActivity : AppCompatActivity(), OnNavigationItemSelectedListener {


    private lateinit var activityListNoteBinding: ActivityListNoteBinding
    private lateinit var drawerBinding: DrawerBinding
    private val viewModel: ListNoteActivityViewModel by viewModels()
    private var logTag = this::class.simpleName
    private val noteRecyclerAdapter by lazy { NoteRecyclerAdapter(this, DataManager.notes) }
    private val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(
            this,
            DataManager.courses.values.toList()
        )
    }
    private lateinit var db: DatabaseHelper
    private val noteLayout by lazy { LinearLayoutManager(this) }
    private val courseLayout by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerBinding = DrawerBinding.inflate(layoutInflater)
        activityListNoteBinding = ActivityListNoteBinding.inflate(layoutInflater)
        setContentView(drawerBinding.root)
        setSupportActionBar(activityListNoteBinding.toolbarNotesList)
        Log.d(logTag, "$logTag has been created")
        if (viewModel.isStarted && savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)
        }
        db = DatabaseHelper(this)
        val portal = db.writableDatabase
        val toInsert = ContentValues()
        toInsert.put(NoteKeeperDBContract.NoteEntry.COLUMN_DATE_CREATED, "today")
        toInsert.put(NoteKeeperDBContract.NoteEntry.COLUMN_NOTE_BODY, "Hi there")
        toInsert.put(NoteKeeperDBContract.NoteEntry.COLUMN_NOTE_COURSE, 3)
        toInsert.put(NoteKeeperDBContract.NoteEntry.COLUMN_NOTE_TITLE, "fkghf")
        portal.insert(NoteKeeperDBContract.NoteEntry.TABLE_NAME, null, toInsert)
        viewModel.isStarted = false
        activityListNoteBinding.addNoteButton.setOnClickListener { _ ->
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, EXTRA_NO_NOTE_POSITION)
            startActivity(activityIntent)
        }

        handleItemSelection(viewModel.navDrawerSelection)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerBinding.activityMainDrawer,
            activityListNoteBinding.toolbarNotesList,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerBinding.activityMainDrawer.addDrawerListener(toggle)
        toggle.syncState()
        drawerBinding.navView.setNavigationItemSelectedListener(this)
    }


    private fun displayNotes() {
        activityListNoteBinding.noteCourseList.layoutManager = noteLayout
        activityListNoteBinding.noteCourseList.adapter = noteRecyclerAdapter
        drawerBinding.navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        activityListNoteBinding.noteCourseList.layoutManager = courseLayout
        activityListNoteBinding.noteCourseList.adapter = courseRecyclerAdapter
        drawerBinding.navView.menu.findItem(R.id.nav_courses).isChecked = true
    }

    private fun handleItemSelection(itemId: Int) {
        when (itemId) {
            R.id.nav_notes -> {
                displayNotes()
            }
            R.id.nav_courses -> {
                displayCourses()
            }
        }
        viewModel.navDrawerSelection = itemId
    }

    fun displayNotesByCourse(course: CourseInfo) {
        activityListNoteBinding.noteCourseList.layoutManager = noteLayout
        activityListNoteBinding.noteCourseList.adapter = NotesUnderCourseAdapter(this, course)
    }

    override fun onBackPressed() {
        if (drawerBinding.activityMainDrawer.isDrawerOpen(GravityCompat.START))
            drawerBinding.activityMainDrawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()

    }

    override fun onResume() {
        super.onResume()
        Log.d(logTag, "$logTag has resumed")
        activityListNoteBinding.noteCourseList.adapter?.notifyDataSetChanged()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(logTag, "$logTag has been restarted")
    }

    override fun onStop() {
        super.onStop()
        Log.d(logTag, "$logTag has been stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "$logTag has been destroyed")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(viewModel.navDrawerSelectionName, viewModel.navDrawerSelection)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_notes,
            R.id.nav_courses -> handleItemSelection(item.itemId)
            R.id.nav_share -> {
                showMessage("You pressed share")
                drawerBinding.activityMainDrawer.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_send -> {
                showMessage("You pressed send")
                drawerBinding.activityMainDrawer.closeDrawer(GravityCompat.START)
                return false
            }
        }
        drawerBinding.activityMainDrawer.closeDrawer(GravityCompat.START)
        return true

    }

    private fun showMessage(message: String) {
        Snackbar.make(activityListNoteBinding.noteCourseList, message, Snackbar.LENGTH_SHORT).show()
    }


}
