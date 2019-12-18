package com.hyunki.bard2.controller

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull

import com.hyunki.bard2.R
import com.hyunki.bard2.model.Song

class LibraryAdapter(private var songList: List<Song>) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {
    private lateinit var listener: FragmentInteractionListener

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): LibraryViewHolder {

        val child = LayoutInflater.from(viewGroup.context).inflate(R.layout.song_itemview, viewGroup, false)
        val context = viewGroup.context
        if (context is FragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + context.getString(R.string.fragment_exception_message))
        }
        return LibraryViewHolder(child)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.onBind(songList[position],listener)
    }

    fun setSongList(songList: List<Song>) {
        this.songList = songList
        notifyDataSetChanged()
    }

    inner class LibraryViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var songTitle: TextView = itemView.findViewById(R.id.library_songTitle_textview)

        fun onBind(song: Song, listener: FragmentInteractionListener?) {
            songTitle.text = song.songTitle
            songTitle.setOnClickListener { listener?.displaySong(song) }
        }
    }
}
