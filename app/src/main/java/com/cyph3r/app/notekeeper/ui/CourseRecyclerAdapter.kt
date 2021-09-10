package com.cyph3r.app.notekeeper.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cyph3r.app.notekeeper.NoteListActivity
import com.cyph3r.app.notekeeper.R
import com.cyph3r.app.notekeeper.database.AppDatabase


class CourseRecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    private val database = Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
        .allowMainThreadQueries().build()
    private val noteDao = database.noteDao()
    private val courseDao = database.courseDao()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_course_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.courseTitle?.text = courseDao.getAllCourses()[position].courseName
        holder.coursePosition = position
    }

    override fun getItemCount() = courseDao.getAllCourses().size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseTitle: TextView? = itemView.findViewById(R.id.course_name)
        var coursePosition = 0

        init {
            itemView.setOnClickListener {
                (context as NoteListActivity).displayNotesByCourse(
                    courseDao.findCourseById(
                        courseDao.getAllCourses()[coursePosition].courseId
                    )
                )
            }
        }

    }
}
