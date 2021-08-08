package com.cyph3r.app.notekeeper

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    val notes = ArrayList<NoteInfo>()


    init {
        initializeCourses()


        initializeNote()
    }
    private fun initializeNote() {
        notes.add(NoteInfo(courses.values.first(), "This my story", "This my song" ))
    }

    fun addNote(course: CourseInfo, noteTitle: String, noteText: String): Int {
        val note = NoteInfo(course, noteTitle, noteText)
        notes.add(note)

        return notes.lastIndex
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