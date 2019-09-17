package com.hyunki.bard2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer

import com.hyunki.bard2.R
import com.hyunki.bard2.viewmodel.ViewModel
import com.hyunki.bard2.controller.LibraryAdapter

import java.util.ArrayList

class LibraryFragment : Fragment() {
    private var viewModel: ViewModel? = null
    private var adapter: LibraryAdapter? = null
    @BindView(R.id.libraryFragment_recyclerview)
    internal var recyclerView: RecyclerView? = null
    @BindView(R.id.libraryFragment_exit_button)
    internal var exitLibraryButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_library, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        adapter = LibraryAdapter(ArrayList())
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(getContext())
        setAdapter()
        exitLibraryButton!!.setOnClickListener { activity?.onBackPressed() }
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
