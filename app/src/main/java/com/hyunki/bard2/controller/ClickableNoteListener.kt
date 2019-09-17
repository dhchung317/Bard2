package com.hyunki.bard2.controller

import com.hyunki.bard2.model.ClickableNote

interface ClickableNoteListener {
    fun setCurrentNote(note: ClickableNote)
}
