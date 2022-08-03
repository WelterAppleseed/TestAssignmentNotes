package com.example.testassignmentnotes.di

import com.example.testassignmentnotes.di.AppComponent
import com.example.testassignmentnotes.ui.list.NoteListViewModel
import dagger.Component

@RootScope
@Component(
    dependencies = [
        AppComponent::class,
    ],
    modules = [
    ]
)
interface RootComponent {

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent
        ): RootComponent
    }

    fun getNoteListViewModel(): NoteListViewModel

}