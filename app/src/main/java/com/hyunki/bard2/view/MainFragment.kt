package com.hyunki.bard2.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup

import com.hyunki.bard2.Animations
import com.hyunki.bard2.R
import com.hyunki.bard2.controller.FragmentInteractionListener
import com.hyunki.bard2.databinding.FragmentMainBinding

class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var listener: FragmentInteractionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentImageView.setImageResource(R.drawable.bardlogo)
        Animations.setDropTitleAnimation(binding.mainFragmentHeaderTextview)
        Animations.setPopUpAnimation(binding.mainFragmentComposeButton)
        Animations.setPopUpAnimation(binding.mainFragmentLibraryButton)
        binding.mainFragmentComposeButton.setOnClickListener(this)
        binding.mainFragmentLibraryButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.mainFragment_compose_button -> listener.displayComposer()
            R.id.mainFragment_library_button -> listener.displayLibrary()
        }
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
