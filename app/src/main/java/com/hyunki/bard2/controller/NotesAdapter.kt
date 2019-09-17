package com.hyunki.bard2.controller

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

import com.hyunki.bard2.R
import com.hyunki.bard2.model.ClickableNote

class NotesAdapter(private var notesList: List<ClickableNote>?) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var listener: ClickableNoteListener? = null
    private var selected_position = 0

    val itemCount: Int
        @Override
        get() = notesList!!.size()

    @NonNull
    @Override
    fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): NotesViewHolder {

        val child = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_itemview, viewGroup, false)
        val context = viewGroup.getContext()
        if (context is ClickableNoteListener) {
            listener = context as ClickableNoteListener
        } else {
            throw RuntimeException(context.toString() + context.getString(R.string.clickable_listener_exception_message))
        }

        return NotesViewHolder(child)
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    fun onBindViewHolder(@NonNull notesViewHolder: NotesViewHolder, i: Int) {
        notesViewHolder.onBind(notesList!![i], listener)
        notesViewHolder.itemView.setBackgroundColor(if (selected_position == i) Color.LTGRAY else Color.TRANSPARENT)
    }

    fun setNotesList(notesList: List<ClickableNote>) {
        this.notesList = notesList
        notifyDataSetChanged()
    }

    inner class NotesViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var noteName: TextView
        internal var noteImage: ImageView

        init {
            noteName = itemView.findViewById(R.id.noteName_textview)
            noteImage = itemView.findViewById(R.id.note_imageView)
        }

        fun onBind(note: ClickableNote, listener: ClickableNoteListener?) {
            noteName.setText(note.getNote())
            noteImage.setImageResource(note.getImgSrc())
            itemView.setOnClickListener(object : View.OnClickListener() {
                @Override
                fun onClick(v: View) {
                    if (getAdapterPosition() === RecyclerView.NO_POSITION) return

                    notifyItemChanged(selected_position)
                    selected_position = getAdapterPosition()
                    notifyItemChanged(selected_position)

                    listener!!.setCurrentNote(note)

                    val mp = MediaPlayer.create(itemView.getContext(), note.getRawNote())
                    mp.seekTo(600)
                    mp.start()
                }
            })
        }
    }
}
