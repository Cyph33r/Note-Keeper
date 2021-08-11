package com.cyph3r.app.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_list_note.*
import kotlinx.android.synthetic.main.drawer.*



class NoteListActivity : AppCompatActivity(), OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer)
//        setSupportActionBar(toolbar_note_list)

        add_note_button.setOnClickListener { _ ->
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, EXTRA_NO_NOTE_POSITION)
            startActivity(activityIntent)
        }
        note_course_list.layoutManager = LinearLayoutManager(this)
//        list_item.layoutManager.
        note_course_list.adapter = NoteRecyclerAdapter(this, DataManager.notes)

    }

    override fun onResume() {
        super.onResume()
        note_course_list.adapter?.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }


}
