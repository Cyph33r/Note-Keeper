package com.cyph3r.app.notekeeper

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cyph3r.app.notekeeper.database.AppDatabase
import com.cyph3r.app.notekeeper.database.Course
import com.cyph3r.app.notekeeper.database.CourseDao
import com.cyph3r.app.notekeeper.database.NoteDao
import com.cyph3r.app.notekeeper.databinding.AddEditNoteDialogBinding
import com.cyph3r.app.notekeeper.databinding.DrawerBinding
import com.cyph3r.app.notekeeper.ui.CourseRecyclerAdapter
import com.cyph3r.app.notekeeper.ui.NoteRecyclerAdapter
import com.cyph3r.app.notekeeper.ui.NotesUnderCourseAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class NoteListActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    lateinit var mAuth: FirebaseAuth
    var currentUser: FirebaseUser? = null
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
    private lateinit var database: AppDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var courseDao: CourseDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        if (currentUser == null) {
            mAuth.createUserWithEmailAndPassword("tadeajai4@gmail.com", "11235")
                .addOnCompleteListener(this) {
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {
                            Log.d("Login Activity", "createUserWithEmail:success")
                            currentUser = mAuth.currentUser
                        } else {
                            Snackbar.make(
                                this@NoteListActivity,
                                drawerBinding.root,
                                "Login Unsuccessful",
                                Snackbar.LENGTH_LONG
                            ).show()

                        }
                    }
                }
        }
        _drawerBinding = DrawerBinding.inflate(layoutInflater)
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database.db")
            .allowMainThreadQueries().build()
        courseDao = database.courseDao()
        noteDao = database.noteDao()
        setContentView(drawerBinding.root)
        setSupportActionBar(drawerBinding.activityListNoteRoot.toolbarNotesList)
        Log.d(logTag, "$logTag has been created")
        if (viewModel.isStarted && savedInstanceState != null) {
            viewModel.restoreState(savedInstanceState)
        }
        viewModel.isStarted = false
        drawerBinding.activityListNoteRoot.addNoteButton.setOnClickListener(::onAddClick)
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
        menuInflater.inflate(R.menu.note_list_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.app_bar_search).actionView as SearchView).apply {
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(
                        this@NoteListActivity.applicationContext,
                        SearchNoteActivity::class.java
                    )
                )
            )
        }
        Snackbar.make(
            drawerBinding.root,
            "${
                searchManager.getSearchableInfo(
                    ComponentName(
                        this@NoteListActivity,
                        this::class.java
                    )
                ) == null
            }",
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings_note_list -> {
//                val launchSettings = Intent(this, SettingsFragment::class.java)
//                startF(launchSettings)

//                deleteNote(1)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteNote(start: Int) {
        drawerBinding.activityListNoteRoot.noteCourseList.adapter!!.notifyItemRemoved(start)
        drawerBinding.activityListNoteRoot.noteCourseList.adapter!!.notifyItemRangeChanged(
            start,
            noteDao.getSize()
        )
        ReminderNotification.notify(
            this,
            "Note Deleted",
            "Note at position $start has been deleted",
            0
        )//todo: change this later
    }

    private fun displayNotes() {
        drawerBinding.activityListNoteRoot.noteCourseList.layoutManager = noteLayout
        drawerBinding.activityListNoteRoot.noteCourseList.adapter = noteRecyclerAdapter
        drawerBinding.navView.menu.findItem(R.id.nav_notes).isChecked = true
        drawerBinding.activityListNoteRoot.addNoteButton.setImageResource(R.drawable.ic_note_add_black_24dp)
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun isItemViewSwipeEnabled() = viewModel.navDrawerSelection == R.id.nav_notes


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteDao.deleteNoteById((viewHolder as NoteRecyclerAdapter.ViewHolder).noteId)
                deleteNote(viewHolder.adapterPosition)
                ReminderNotification.notify(
                    this@NoteListActivity,
                    "Note Deleted",
                    "You have deleted a note",
                    1
                )
            }
        })
        itemTouchHelper.attachToRecyclerView(drawerBinding.activityListNoteRoot.noteCourseList)


    }

    private fun displayCourses() {
        drawerBinding.activityListNoteRoot.noteCourseList.layoutManager = courseLayout
        drawerBinding.activityListNoteRoot.noteCourseList.adapter = courseRecyclerAdapter
        drawerBinding.navView.menu.findItem(R.id.nav_courses).isChecked = true
        drawerBinding.activityListNoteRoot.addNoteButton.setImageResource(R.drawable.ic_add_note_black_24dp)
    }

    private fun onAddClick(view: View) {
        if (viewModel.navDrawerSelection != R.id.nav_courses) {
            if (courseDao.getAllCourses().isEmpty())
                showMessage("You don't have any course. Please create one to add new note")
            else {
                val activityIntent = Intent(this, EditNoteActivity::class.java)
                activityIntent.putExtra(NOTE_ID, EXTRA_NO_NOTE_ID)
                startActivity(activityIntent)
            }
        } else {
            addCourse()
        }
    }

    private fun addCourse() {
        val addEditNoteDialogBinding = AddEditNoteDialogBinding.inflate(layoutInflater)
        val dialogTitle = addEditNoteDialogBinding.editCourseDialogTitle
        val dialogCourseId = addEditNoteDialogBinding.editCourseDialogCourseId
        val dialogCourseName = addEditNoteDialogBinding.editCourseDialogName
        val builder = AlertDialog.Builder(this)
        builder.setView(addEditNoteDialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                courseDao.addCourse(
                    Course(
                        dialogCourseId.text.toString(),
                        dialogCourseName.text.toString()
                    )
                )
                drawerBinding.activityListNoteRoot.noteCourseList.adapter?.notifyDataSetChanged()
                showMessage("Course Added")
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
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
        drawerBinding.activityListNoteRoot.addNoteButton.setImageResource(R.drawable.ic_note_add_black_24dp)
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
