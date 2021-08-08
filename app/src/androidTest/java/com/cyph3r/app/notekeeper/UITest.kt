package com.cyph3r.app.notekeeper

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard


@RunWith(AndroidJUnit4::class)
class UITest {


    @JvmField
    @Rule
    val noteListActivity = ActivityTestRule(NoteListActivity::class.java)


    fun createNewNote(i: Int) {


        onView(withId(R.id.add_note_button)).perform(click())
        onView(withId(R.id.spinner_courses)).perform(click())
        when (Random.nextInt(4)) {
            0 -> {
                onData(
                    allOf(
                        instanceOf(CourseInfo::class.java),
                        equalTo(DataManager.courses.values.elementAt(0))
                    )
                ).perform(click())
            }
            1 -> {
                onData(
                    allOf(
                        instanceOf(CourseInfo::class.java),
                        equalTo(DataManager.courses.values.elementAt(1))
                    )
                ).perform(click())
            }
            2 -> {
                onData(
                    allOf(
                        instanceOf(CourseInfo::class.java),
                        equalTo(DataManager.courses.values.elementAt(2))
                    )
                ).perform(click())
            }
            3 -> {
                onData(
                    allOf(
                        instanceOf(CourseInfo::class.java),
                        equalTo(DataManager.courses.values.elementAt(3))
                    )
                ).perform(click())
            }

        }
        onView(withId(R.id.field_note_title)).perform(typeText("Note $i"))
        onView(withId(R.id.field_note_text)).perform(
            typeText("hEllo there!!!!!!!!"),
            closeSoftKeyboard()
        )
        pressBack()


    }

    @Test
    fun createMultipleNotes() {
        for (ii in 1..20) {
            createNewNote(ii)
        }
        7

    }
}