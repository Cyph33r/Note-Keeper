package com.cyph3r.app.notekeeper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NoteRecyclerAdapter(private val context: Context, private val notes: List<NoteInfo>) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.noteTitle?.text = note.course?.title
        holder.noteText?.text = if (note.title.isNullOrBlank()) "<Unnamed>" else note.title
        holder.notePosition = position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateCreated: TextView? = itemView.findViewById(R.id.date_created_text)
        val noteTitle: TextView? = itemView.findViewById(R.id.list_note_title)
        val noteText: TextView? = itemView.findViewById(R.id.list_note_text)
        var notePosition = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, EditNoteActivity::class.java)
                intent.putExtra(NOTE_POSITION, notePosition)
                context.startActivity(intent)

            }
        }
    }
}



















