package com.cyph3r.app.notekeeper

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel

//
//
//
//
//
//
class ListNoteActivityViewModel : ViewModel(){

    var isStarted: Boolean = true
    val navDrawerSelectionName = "${this::class.java.simpleName}.navDrawerSelection"
    var navDrawerSelection = R.id.nav_notes

    fun restoreState(bundle: Bundle) {
        navDrawerSelection = bundle.getInt(navDrawerSelectionName)
    }

}