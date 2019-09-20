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
import com.hyunki.bard2.databinding.FragmentLibraryBinding

import java.util.ArrayList

class LibraryFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentLibraryBinding
    private lateinit var viewModel: ViewModel
    private lateinit var adapter: LibraryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LibraryAdapter(ArrayList())
        binding.libraryFragmentRecyclerview.adapter = adapter
        binding.libraryFragmentRecyclerview.layoutManager = LinearLayoutManager(context)
        setAdapter()

        binding.libraryFragmentExitButton.setOnClickListener(this)
    }

    override fun onClick(v:View?){
        when(v?.id){
            R.id.libraryFragment_exit_button -> activity?.onBackPressed()
        }
    }

    private fun setAdapter() {
        viewModel.allSongs.observe(this, Observer { adapter.setSongList(it) })
    }

    companion object {
        fun newInstance(): LibraryFragment {
            return LibraryFragment()
        }
    }
}
