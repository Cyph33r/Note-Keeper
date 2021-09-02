package com.cyph3r.app.notekeeper

import com.cyph3r.app.notekeeper.DataManager.notes

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    val notes = ArrayList<NoteInfo>()


    init {
        initializeCourses()


        initializeNote()
    }

    private fun initializeNote() {
        for (i in (courses.values))
            for (j in 1..3)
                notes.add(NoteInfo(i, "This my story $j", "This my song $i"))
    }

    fun addNote(course: CourseInfo, noteTitle: String, noteText: String): Int {
        val note = NoteInfo(course, noteTitle, noteText)
        notes.add(note)

        return notes.lastIndex
    }

    fun getNotesUnderCourse(course: CourseInfo): List<NoteInfo> {
        val toReturn = mutableListOf<NoteInfo>()
        for (note in notes)
            if (note.course == course)
                toReturn.add(note)

        return toReturn.toList()
    }

    private fun initializeCourses() {
        var course = CourseInfo("android_intents", "Android Programming with Intents")
        courses[course.courseId] = course
        course = CourseInfo("android_async", "Android Async Programming and Services")
        courses[course.courseId] = course
        course = CourseInfo(title = "Java Fundamentals in Java Language", courseId = "java_lang")
        courses[course.courseId] = course
        course = CourseInfo("java_core", "Java Fundamental: The Core Platform")
        courses.set(course.courseId, course)
    }
}