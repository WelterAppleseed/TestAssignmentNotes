package com.example.testassignmentnotes.di

import android.app.Application
import com.example.testassignmentnotes.ui.list.NoteListViewModel

class DependencyManager private constructor(
    application: Application
) {
    private val appComponent = DaggerAppComponent.factory().create(application)

    private val rootComponent = DaggerRootComponent.factory().create(appComponent)

    companion object {
        private lateinit var instance: DependencyManager
        fun init(application: Application) {
            instance = DependencyManager(application)
        }
        fun noteListViewModel(): NoteListViewModel = instance.rootComponent.getNoteListViewModel()
    }

}
