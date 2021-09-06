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
import com.cyph3r.app.notekeeper.databinding.DrawerBinding
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar


class NoteListActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private var _drawerBinding: DrawerBinding? = null
    private val drawerBinding: DrawerBinding get() = _drawerBinding!!
    private val viewModel: ListNoteActivityViewModel by viewModels()
    private var logTag = this::class.simpleName
    private val noteRecyclerAdapter by lazy {
        NoteRecyclerAdapter(
            this,
            DataManager.notes,
            lifecycle
        )
    }
    private val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(
            this,
            DataManager.courses.values.toList()
        )
    }
    private val noteLayout by lazy { LinearLayoutManager(this) }
    private val courseLayout by lazy { LinearLayoutManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _drawerBinding = DrawerBinding.inflate(layoutInflater)
        setContentView(drawerBinding.root)
        setSupportActionBar(drawerBinding.activityListNoteRoot.toolbarNotesList)
        Log.d(logTag, "$logTag has been created")
        if (viewModel.isStarted && savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)
        }
        viewModel.isStarted = false
        drawerBinding.activityListNoteRoot.addNoteButton.setOnClickListener {
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, EXTRA_NO_NOTE_POSITION)
            startActivity(activityIntent)
        }
        DataManager.database = DatabaseHelper(this)
        handleItemSelection(viewModel.navDrawerSelection)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerBinding.activityMainDrawer,
            drawerBinding.activityListNoteRoot.toolbarNotesList,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerBinding.activityMainDrawer.addDrawerListener(toggle)
        toggle.syncState()
        drawerBinding.navView.setNavigationItemSelectedListener(this)
    }


    private fun displayNotes() {
        drawerBinding.activityListNoteRoot.noteCourseList.layoutManager = noteLayout
        drawerBinding.activityListNoteRoot.noteCourseList.adapter = noteRecyclerAdapter
        drawerBinding.navView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    private fun displayCourses() {
        drawerBinding.activityListNoteRoot.noteCourseList.layoutManager = courseLayout
        drawerBinding.activityListNoteRoot.noteCourseList.adapter = courseRecyclerAdapter
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
        drawerBinding.activityListNoteRoot.noteCourseList.layoutManager = noteLayout
        drawerBinding.activityListNoteRoot.noteCourseList.adapter =
            NotesUnderCourseAdapter(this, lifecycle, course)
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
        drawerBinding.activityListNoteRoot.noteCourseList.adapter?.notifyDataSetChanged()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(logTag, "$logTag has been restarted")
    }

    override fun onStop() {
        Log.d(logTag, "$logTag has been stopped")
        super.onStop()
    }

    override fun onDestroy() {
        _drawerBinding = null
        Log.d(logTag, "$logTag has been destroyed")
        super.onDestroy()
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

    fun showMessage(message: String) {
        Snackbar.make(
            drawerBinding.activityListNoteRoot.addNoteButton,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }


}
