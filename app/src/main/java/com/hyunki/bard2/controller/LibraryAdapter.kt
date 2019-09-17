package com.hyunki.bard2.controller

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.hyunki.bard2.R
import com.hyunki.bard2.model.Song

class LibraryAdapter(private var songList: List<Song>?) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {
    private var listener: FragmentInteractionListener? = null

    val itemCount: Int
        @Override
        get() = songList!!.size()

    @NonNull
    @Override
    fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): LibraryViewHolder {

        val child = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_itemview, viewGroup, false)
        val context = viewGroup.getContext()
        if (context is FragmentInteractionListener) {
            listener = context as FragmentInteractionListener
        } else {
            throw RuntimeException(context.toString() + context.getString(R.string.fragment_exception_message))
        }
        return LibraryViewHolder(child)
    }

    @Override
    fun onBindViewHolder(@NonNull libraryViewHolder: LibraryViewHolder, i: Int) {
        libraryViewHolder.onBind(songList!![i], listener)
    }

    fun setSongList(songList: List<Song>) {
        this.songList = songList
        notifyDataSetChanged()
    }

    inner class LibraryViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var songTitle: TextView

        init {
            songTitle = itemView.findViewById(R.id.library_songTitle_textview)
        }

        fun onBind(song: Song, listener: FragmentInteractionListener?) {
            songTitle.setText(song.getSongTitle())
            songTitle.setOnClickListener({ v -> listener!!.displaySong(song) })
        }
    }
}
