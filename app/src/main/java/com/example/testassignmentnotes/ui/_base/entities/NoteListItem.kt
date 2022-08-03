package com.example.testassignmentnotes.ui._base.entities

import java.time.LocalDateTime

data class NoteListItem(
    val id: Long?,
    val title: String?,
    val content: String?,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)