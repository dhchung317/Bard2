package com.hyunki.bard2.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.hyunki.bard2.Animations
import com.hyunki.bard2.R
import com.hyunki.bard2.controller.FragmentInteractionListener

class MainFragment : Fragment(), View.OnClickListener {
    @BindView(R.id.mainFragment_header_textview)
    internal var mainTitle: TextView? = null
    @BindView(R.id.mainFragment_imageView)
    internal var logo: ImageView? = null
    @BindView(R.id.mainFragment_compose_button)
    internal var compose: Button? = null
    @BindView(R.id.main_songlist_button)
    internal var library: Button? = null
    private var listener: FragmentInteractionListener? = null

    @Override
    fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentInteractionListener) {
            listener = context as FragmentInteractionListener
        }
    }

    @Nullable
    @Override
    fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup, @Nullable savedInstanceState: Bundle): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @Override
    fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        logo!!.setImageResource(R.drawable.bardlogo)
        Animations.setDropTitleAnimation(mainTitle)
        Animations.setPopUpAnimation(compose)
        Animations.setPopUpAnimation(library)
    }

    @OnClick(R.id.mainFragment_compose_button, R.id.main_songlist_button)
    @Override
    fun onClick(v: View) {
        when (v.getId()) {
            R.id.mainFragment_compose_button -> listener!!.displayComposer()
            R.id.main_songlist_button -> listener!!.displayLibrary()
        }
    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
