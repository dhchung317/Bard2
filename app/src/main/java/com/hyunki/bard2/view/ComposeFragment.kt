package com.hyunki.bard2.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.hyunki.bard2.R
import com.hyunki.bard2.controller.FragmentInteractionListener
import com.hyunki.bard2.controller.NotesAdapter
import com.hyunki.bard2.model.ClickableNote
import com.hyunki.bard2.model.Note
import com.hyunki.bard2.model.Song
import com.hyunki.bard2.viewmodel.ViewModel

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyunki.bard2.databinding.FragmentComposeBinding

import java.util.ArrayList

class ComposeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding:FragmentComposeBinding
    private var listener: FragmentInteractionListener? = null
    private var viewModel: ViewModel? = null
    private var song: Song? = null
    private var rawId: Int? = 0
    private var noteName: String? = null
    private var defaultDuration: String? = null
    private var adapter: NotesAdapter? = null
    private val clickableNotes = ArrayList<ClickableNote>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultDuration = "1000"
        viewModel = ViewModelProviders.of(this.activity!!).get(ViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentComposeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        song = Song()
        adapter = NotesAdapter(clickableNotes)
        binding.composeRecyclerview.adapter = adapter
        binding.composeRecyclerview.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        val noteRes = resources.getStringArray(R.array.spinner_array)
        for (i in noteRes.indices) {
            clickableNotes.add(ClickableNote(
                    noteRes[i],
                    activity?.resources?.getIdentifier(
                            noteRes[i].toLowerCase(),
                            "raw",
                            activity!!.packageName
                    ),
                    activity?.resources?.getIdentifier(
                            "alto_" + noteRes[i].toLowerCase(),
                            "drawable",
                            context?.packageName)

            ))
        }
        viewModel!!.currentNote = clickableNotes[0]
        adapter!!.setNotesList(clickableNotes)

        binding.composeFragmentAddSongButton.setOnClickListener(this)
        binding.composeFragmentAddNoteButton.setOnClickListener(this)
        binding.composeFragmentDeleteNoteButton.setOnClickListener(this)
        binding.composeFragmentLibraryButton.setOnClickListener(this)
    }

    private fun deleteNotes() {
        if (song!!.songNotes.isNotEmpty()) {
            song!!.deleteNote()
        }

        var onDeleteCurrentNotesDisplay = ""
        for (n in song!!.songNotes) {
            if (binding.composeFragmentDisplayCurrentNotesTextView.text.toString().isEmpty()) {
                onDeleteCurrentNotesDisplay = n.note + " "
            } else {
                onDeleteCurrentNotesDisplay += n.note + " "
            }
        }
        binding.composeFragmentDisplayCurrentNotesTextView.text = onDeleteCurrentNotesDisplay
    }

    private fun addNotes() {
        val currentNote = viewModel!!.currentNote
        Log.d("danny", viewModel!!.currentNote.toString())

        rawId = currentNote?.rawNote
        noteName = currentNote?.note

        var durationI = defaultDuration

        if (binding.composeFragmentDurationEditText.text.toString().isEmpty()) {
            Toast.makeText(activity, getString(R.string.no_duration_entered_message), Toast.LENGTH_SHORT).show()
        } else {
            durationI = binding.composeFragmentDurationEditText.text.toString()
        }
        song!!.addNote(Note(
                rawId,
                binding.composeFragmentSyllableEditText.text.toString(),
                Integer.parseInt(durationI.toString()),
                noteName)
        )
        binding.composeFragmentDisplayCurrentNotesTextView.append(String.format("%s ", noteName))
    }

    private fun addSong() {
        when {
            binding.composeFragmentSongTitleEditText.text.toString().isEmpty() -> Toast.makeText(activity, getString(R.string.no_title_entered_message), Toast.LENGTH_SHORT).show()

            viewModel?.getRepository()?.getSong(
                    binding.composeFragmentSongTitleEditText.text.toString())?.songTitle
                    == binding.composeFragmentSongTitleEditText.text.toString()
            -> {
                val logSong = viewModel?.getRepository()?.getSong(binding.composeFragmentSongTitleEditText.text.toString())

                Toast.makeText(activity, getString(R.string.title_exists_message), Toast.LENGTH_SHORT).show()

                Log.d("get from database",
                        logSong?.songTitle)
            }else -> {
                song!!.songTitle = binding.composeFragmentSongTitleEditText.text.toString()
                viewModel!!.addSong(song!!)
                Toast.makeText(activity, getString(R.string.song_added_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.composeFragment_addSong_button -> addSong()
            R.id.composeFragment_addNote_button -> addNotes()
            R.id.composeFragment_deleteNote_button -> deleteNotes()
            R.id.composeFragment_library_button -> listener?.displayLibrary()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clickableNotes.clear()
    }

    companion object {
        fun newInstance(): ComposeFragment {
            return ComposeFragment()
        }
    }
}
