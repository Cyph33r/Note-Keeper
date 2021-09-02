package com.cyph3r.app.notekeeper

import org.junit.Test
import org.junit.Assert.*

class GetNotesUnderCourseTest {

    @Test
    fun getNotesUnderCourse() {
        val d = DataManager.getNotesUnderCourse(DataManager.courses.values.elementAt(0))
        print(d)
    }
}