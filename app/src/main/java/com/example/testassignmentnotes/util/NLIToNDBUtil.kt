package com.example.testassignmentnotes.util

import com.example.testassignmentnotes.data.NoteDbo
import com.example.testassignmentnotes.ui._base.entities.NoteListItem

fun NoteListItem.asNoteDbo(): NoteDbo {
    return if (this.id == null) { NoteDbo(
        title = this.title!!,
        content =  this.content ?: "",
        createdAt = this.createdAt!!,
        modifiedAt = this.modifiedAt!!
    ) } else {
        NoteDbo(
            id = this.id,
            title = this.title!!,
            content =this.content ?: "",
            createdAt = this.createdAt!!,
            modifiedAt = this.modifiedAt!!
        )
    }
}