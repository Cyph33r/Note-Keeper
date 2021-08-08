package com.cyph3r.app.notekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_note.*

class NoteListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_note)
//        setSupportActionBar(toolbar_list_note)

        add_note_button.setOnClickListener { _ ->
            val activityIntent = Intent(this, EditNoteActivity::class.java)
            activityIntent.putExtra(NOTE_POSITION, EXTRA_NO_NOTE_POSITION)
            startActivity(activityIntent)
        }
        list_item.layoutManager = LinearLayoutManager(this)
//        list_item.layoutManager.
        list_item.adapter = NoteRecyclerAdapter(this, DataManager.notes)

    }

    override fun onResume() {
        super.onResume()
        list_item.adapter?.notifyDataSetChanged()
    }


}
