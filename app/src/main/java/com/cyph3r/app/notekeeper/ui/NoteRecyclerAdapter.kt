package com.cyph3r.app.notekeeper.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cyph3r.app.notekeeper.EditNoteActivity
import com.cyph3r.app.notekeeper.NOTE_ID
import com.cyph3r.app.notekeeper.R
import com.cyph3r.app.notekeeper.database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

//todo:Add callback to update view on resume
class NoteRecyclerAdapter(
    private val context: Context) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>(), LifecycleObserver {
    private val tag = this::class.simpleName
    private val layoutInflater = LayoutInflater.from(context)
    private val database = Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
        .allowMainThreadQueries().build()
    private val noteDao = database.noteDao()
    private val courseDao = database.courseDao()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = noteDao.getAllNotes().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteDao.getAllNotes()[position]
        val noteCourse = courseDao.findCourseById(note.noteCourseId)
        holder.noteTitle?.text = noteCourse.courseName
        holder.noteText?.text = if (note.noteTitle.isBlank()) "<Unnamed>" else note.noteTitle
        holder.dateCreated?.text =
            SimpleDateFormat(
                "EEE, d MMM yyyy HH:mm:ss",
                Locale.US
            ).format(Date(note.dateCreated)) + " $position"
        holder.notePosition = position.toLong()
        /* if (position in 0..2)
             Log.d(tag, "Position is $position")*/
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateCreated: TextView? = itemView.findViewById(R.id.date_created_text)
        val noteTitle: TextView? = itemView.findViewById(R.id.list_note_title)
        val noteText: TextView? = itemView.findViewById(R.id.list_note_text)
        var notePosition = 0L

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, EditNoteActivity::class.java)
                intent.putExtra(NOTE_ID, noteDao.getAllNotes()[notePosition.toInt()].NoteId)
                context.startActivity(intent)


            }
        }
    }
}



















