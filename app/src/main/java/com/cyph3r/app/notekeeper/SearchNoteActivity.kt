package com.cyph3r.app.notekeeper

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyph3r.app.notekeeper.ui.SearchNoteAdapter
import kotlinx.android.synthetic.main.activity_search_note.*

class SearchNoteActivity : AppCompatActivity() {
    lateinit var adapter: SearchNoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_note)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_list_menu, menu)
        return true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            Toast.makeText(this, "You searched $query", Toast.LENGTH_LONG).show()
            search_note_recycler.adapter = SearchNoteAdapter(this, layoutInflater, query!!)
            search_note_recycler.layoutManager=LinearLayoutManager(this)
        }
    }
//    ...
}
