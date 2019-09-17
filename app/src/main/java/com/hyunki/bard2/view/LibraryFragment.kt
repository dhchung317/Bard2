package com.hyunki.bard2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.hyunki.bard2.R
import com.hyunki.bard2.viewmodel.ViewModel
import com.hyunki.bard2.controller.LibraryAdapter
import kotlinx.android.synthetic.main.fragment_library.*

import java.util.ArrayList

class LibraryFragment : Fragment(), View.OnClickListener {
    private var viewModel: ViewModel? = null
    private var adapter: LibraryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LibraryAdapter(ArrayList())
        libraryFragment_recyclerview!!.adapter = adapter
        libraryFragment_recyclerview!!.layoutManager = LinearLayoutManager(getContext())
        setAdapter()

        libraryFragment_exit_button.setOnClickListener(this)
    }

    override fun onClick(v:View?){
        when(v?.id){
            R.id.libraryFragment_exit_button -> activity?.onBackPressed()
        }
    }

    private fun setAdapter() {
        viewModel!!.allSongs.observe(this, Observer { adapter!!.setSongList(it) })
    }

    companion object {
        fun newInstance(): LibraryFragment {
            return LibraryFragment()
        }
    }
}
