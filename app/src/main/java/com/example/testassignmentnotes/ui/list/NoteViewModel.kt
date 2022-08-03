package com.example.testassignmentnotes.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testassignmentnotes.ui._base.entities.NoteListItem

class NoteViewModel: ViewModel() {
    private val _note = MutableLiveData<NoteListItem?>()

    fun getNote() = _note

    fun updateNote(note: NoteListItem?) {
        _note.postValue(note)
    }
}