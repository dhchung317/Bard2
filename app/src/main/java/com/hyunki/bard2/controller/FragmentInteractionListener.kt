package com.hyunki.bard2.controller

import com.hyunki.bard2.model.Song

interface FragmentInteractionListener {
    fun displaySong(song: Song)

    fun displayComposer()

    fun displayLibrary()

}
