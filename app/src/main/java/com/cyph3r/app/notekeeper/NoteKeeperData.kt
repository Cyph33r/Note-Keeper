package com.cyph3r.app.notekeeper


data class CourseInfo(val courseId: String, var title: String) {
    override fun toString() = title

}


data class NoteInfo(
    var course: CourseInfo? = null,
    var title: String? = null,
    var text: String? = null,
    var dateCreated: Long = System.currentTimeMillis()
) {
}