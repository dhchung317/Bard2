package com.hyunki.bard2.controller

import android.graphics.Color
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.hyunki.bard2.R
import com.hyunki.bard2.model.ClickableNote

class NotesAdapter(private var notesList: List<ClickableNote>) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private lateinit var listener: ClickableNoteListener
    private var selectedPosition = 0

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NotesViewHolder {
        val child = LayoutInflater.from(viewGroup.context).inflate(R.layout.note_itemview, viewGroup, false)
        val context = viewGroup.context
        if (context is ClickableNoteListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + context.getString(R.string.clickable_listener_exception_message))
        }

        return NotesViewHolder(child)
    }

    override fun onBindViewHolder(notesViewHolder: NotesViewHolder, i: Int) {
        notesViewHolder.onBind(notesList[i], listener)
        notesViewHolder.itemView.setBackgroundColor(if (selectedPosition == i) Color.LTGRAY else Color.TRANSPARENT)
    }

    fun setNotesList(notesList: List<ClickableNote>) {
        this.notesList = notesList
        notifyDataSetChanged()
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var noteName: TextView = itemView.findViewById(R.id.noteName_textview)
        private var noteImage: ImageView = itemView.findViewById(R.id.note_imageView)

        fun onBind(note: ClickableNote, listener: ClickableNoteListener?) {
            noteName.text = note.note
            noteImage.setImageResource(note.imgSrc)
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (adapterPosition == RecyclerView.NO_POSITION) return

                    notifyItemChanged(selectedPosition)
                    selectedPosition = adapterPosition
                    notifyItemChanged(selectedPosition)

                    listener?.setCurrentNote(note)

                    val mp = MediaPlayer.create(itemView.context, note.rawNote)
                    mp.seekTo(600)
                    mp.start()
                }
            })
        }
    }
}
