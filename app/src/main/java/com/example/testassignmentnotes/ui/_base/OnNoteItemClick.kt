package com.example.testassignmentnotes.ui._base

import com.example.testassignmentnotes.ui._base.entities.NoteListItem

interface OnNoteItemClick {
    fun onNoteClick(note: NoteListItem)
}