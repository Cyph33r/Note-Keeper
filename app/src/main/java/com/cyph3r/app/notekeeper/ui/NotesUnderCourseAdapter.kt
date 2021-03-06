package com.cyph3r.app.notekeeper.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cyph3r.app.notekeeper.EditNoteActivity
import com.cyph3r.app.notekeeper.NOTE_ID
import com.cyph3r.app.notekeeper.R
import com.cyph3r.app.notekeeper.database.AppDatabase
import com.cyph3r.app.notekeeper.database.Course
import java.text.SimpleDateFormat
import java.util.*

class NotesUnderCourseAdapter(
    private val context: Context,
    private val course: Course
) : RecyclerView.Adapter<NotesUnderCourseAdapter.ViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val layoutInflater = LayoutInflater.from(context)
    private val database = Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
        .allowMainThreadQueries().build()
    private val noteDao = database.noteDao()
    private val courseDao = database.courseDao()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteDao.getNotesByCourse(course.courseID)[position]
        val noteCourse = courseDao.findCourseById(note.noteCourseId)
        holder.noteTitle?.text = noteCourse.courseName
        holder.noteText?.text = if (note.noteTitle.isBlank()) "<Unnamed>" else note.noteTitle
        holder.dateCreated?.text =
            SimpleDateFormat(
                "EEE, d MMM yyyy HH:mm:ss",
                Locale.US
            ).format(Date(note.dateCreated))
        holder.notePosition = position

    }

    override fun getItemCount() = noteDao.getNotesByCourse(course.courseID).size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateCreated: TextView? = itemView.findViewById(R.id.date_created_text)
        val noteTitle: TextView? = itemView.findViewById(R.id.list_note_title)
        val noteText: TextView? = itemView.findViewById(R.id.list_note_text)
        var notePosition = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, EditNoteActivity::class.java)
                intent.putExtra(
                    NOTE_ID,
                    noteDao.getNotesByCourse(course.courseID)[notePosition].NoteId
                )
                context.startActivity(intent)
            }
        }

    }
}
