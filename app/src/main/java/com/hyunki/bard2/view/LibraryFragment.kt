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

    @Override
    fun onCreate(@Nullable savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    @Nullable
    @Override
    fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup, @Nullable savedInstanceState: Bundle): View {
        return inflater.inflate(R.layout.fragment_library, container, false)

    }

    @Override
    fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        adapter = LibraryAdapter(ArrayList())
        recyclerView!!.setAdapter(adapter)
        recyclerView!!.setLayoutManager(LinearLayoutManager(getContext()))
        setAdapter()
        exitLibraryButton!!.setOnClickListener({ v -> getActivity().onBackPressed() })
    }

    fun setAdapter() {
        if (viewModel!!.getAllSongs() != null) {
            viewModel!!.getAllSongs().observe(getViewLifecycleOwner(), { songs -> adapter!!.setSongList(songs) })
        }
    }

    companion object {

        fun newInstance(): LibraryFragment {
            return LibraryFragment()
        }
    }
}
