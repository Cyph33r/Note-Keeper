package com.cyph3r.app.notekeeper

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class NoteRecyclerAdapter(
    private val context: Context,
    private var notes: List<NoteInfo>,
    private val lifecycle: Lifecycle
) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>(), LifecycleObserver {
    private val tag = this::class.simpleName

    init {
        lifecycle.addObserver(this)
    }

    private val layoutInflater = LayoutInflater.from(context)

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun refreshNotes() {
        notes = DataManager.notes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.noteTitle?.text = note.course?.title
        holder.noteText?.text = if (note.title.isNullOrBlank()) "<Unnamed>" else note.title
        holder.dateCreated?.text =
            SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US).format(Date(note.dateCreated))+" $position"
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
                intent.putExtra(NOTE_POSITION, notePosition - 1)
                context.startActivity(intent)

            }
        }
    }
}



















