package com.example.testassignmentnotes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testassignmentnotes.data.NoteDatabase
import com.example.testassignmentnotes.ui._base.entities.NoteListItem
import com.example.testassignmentnotes.util.asNoteDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class NoteListViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {
    private val _notes = MutableLiveData<List<NoteListItem>?>()

    private val _navigateToNote = MutableLiveData<Unit?>()
    val navigateToNote: LiveData<Unit?> = _navigateToNote

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _notes.postValue(
                noteDatabase.noteDao().getAll().map {
                    NoteListItem(
                        id = it.id,
                        title = it.title,
                        content = it.content,
                        createdAt = it.createdAt,
                        modifiedAt = it.modifiedAt
                    )
                }
            )
        }
    }

    fun getAllNotes(): LiveData<List<NoteListItem>?> {
        return _notes
    }

    fun onClick() {
        _navigateToNote.postValue(Unit)
    }
    fun insertDbNote(note: NoteListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().insertNote(note.asNoteDbo())
        }
    }
    fun deleteDbNote(note: NoteListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().remove(note.asNoteDbo())
        }
    }
}
