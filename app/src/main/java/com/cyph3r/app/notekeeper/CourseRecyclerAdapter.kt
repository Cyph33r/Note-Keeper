package com.cyph3r.app.notekeeper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CourseRecyclerAdapter(private val context: Context, private val courses: List<CourseInfo>) :
    RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courseTitle?.text = courses[position].courseId
        holder.courseText?.text = courses[position].title
        holder.coursePosition = position
    }

    override fun getItemCount() = DataManager.courses.values.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseTitle: TextView? = itemView.findViewById<TextView?>(R.id.list_note_title)
        val courseText: TextView? = itemView.findViewById<TextView?>(R.id.list_note_text)
        var coursePosition = 0

        init {
            itemView.setOnClickListener {
                (context as NoteListActivity).showMessage("You have not implemented this yet")
            }
        }

    }
}
