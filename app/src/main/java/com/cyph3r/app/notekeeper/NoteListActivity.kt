package com.cyph3r.app.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.util.Log
import android.view.Menu
import androidx.activity.result.ActivityResultCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.cyph3r.app.notekeeper.database.AppDatabase
import com.cyph3r.app.notekeeper.database.Course
import com.cyph3r.app.notekeeper.database.CourseDao
import com.cyph3r.app.notekeeper.databinding.DrawerBinding
import com.cyph3r.app.notekeeper.ui.CourseRecyclerAdapter
import com.cyph3r.app.notekeeper.ui.NoteRecyclerAdapter
import com.cyph3r.app.notekeeper.ui.NotesUnderCourseAdapter
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar


class NoteListActivity : AppCompatActivity(), OnNavigationItemSelectedListener, ActivityResultCallback<Any> {
    private var _drawerBinding: DrawerBinding? = null
    private val drawerBinding: DrawerBinding get() = _drawerBinding!!
    private val viewModel: ListNoteActivityViewModel by viewModels()
    private var logTag = this::class.simpleName
    private val noteRecyclerAdapter by lazy {
        NoteRecyclerAdapter(this)
    }
    private val courseRecyclerAdapter by lazy {
        CourseRecyclerAdapter(this)
    }
    private val noteLayout by lazy { LinearLayoutManager(this) }
    private val courseLayout by lazy { LinearLayoutManager(this) }
    private lateinit var database:AppDatabase
//    private val noteDao = database.noteDao()
    private lateinit var courseDao :  CourseDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _drawerBinding = DrawerBinding.inflate(layoutInflater)
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "database.db")
            .allowMainThreadQueries().build()
        courseDao = database.courseDao()
        setContentView(drawerBinding.root)
        setSupportActionBar(drawerBinding.activityListNoteRoot.toolbarNotesList)
        Log.d(logTag, "$logTag has been created")
        if (viewModel.isStarted && savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)
        }
        viewModel.isStarted = false
        drawerBinding.activityListNoteRoot.addNoteButton.setOnClickListener {
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            activityIntent.putExtra(NOTE_ID, EXTRA_NO_NOTE_ID)
            startActivity(activityIntent)
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                deleteNote(1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteNote(start: Int) {
        drawerBinding.activityListNoteRoot.noteCourseList.adapter!!.notifyItemRemoved(start)
        drawerBinding.activityListNoteRoot.noteCourseList.adapter!!.notifyItemRangeChanged(
            start,
            drawerBinding.activityListNoteRoot.noteCourseList.adapter!!.itemCount
        )
        ReminderNotification.notify(this, "Hello", "Kitty", 0)
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

    fun displayNotesByCourse(course: Course) {
        drawerBinding.activityListNoteRoot.noteCourseList.layoutManager = noteLayout
        drawerBinding.activityListNoteRoot.noteCourseList.adapter =
            NotesUnderCourseAdapter(this, course)
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

    override fun onActivityResult(result: Any?) {
        TODO("Not yet implemented")
    }


}
