package com.cyph3r.app.notekeeper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesUnderCourseAdapter(private val context: Context, private val course: CourseInfo) :
    RecyclerView.Adapter<NotesUnderCourseAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notesUnderCourse = DataManager.getNotesUnderCourse(course)
        val note = notesUnderCourse[position]
        holder.noteTitle?.text = note.course?.title
        holder.noteText?.text = if (note.title.isNullOrBlank()) "<Unnamed>" else note.title
        holder.notePosition = position

    }

    override fun getItemCount() = DataManager.getNotesUnderCourse(course).size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView? = itemView.findViewById(R.id.list_note_title)
        val noteText: TextView? = itemView.findViewById(R.id.list_note_text)
        var notePosition = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, EditNoteActivity::class.java)
                intent.putExtra(
                    NOTE_POSITION,
                    DataManager.notes.indexOf(DataManager.getNotesUnderCourse(course)[notePosition])
                )
                context.startActivity(intent)
            }
        }

    }
}
