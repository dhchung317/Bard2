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
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var listener: FragmentInteractionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainFragment_imageView.setImageResource(R.drawable.bardlogo)
        Animations.setDropTitleAnimation(mainFragment_header_textview)
        Animations.setPopUpAnimation(mainFragment_compose_button)
        Animations.setPopUpAnimation(mainFragment_library_button)
        mainFragment_library_button.setOnClickListener(this)
        mainFragment_compose_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
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
