package com.hyunki.bard2

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView

object Animations {
    private var dropImage: Animation? = null
    private var dropTitle: Animation? = null
    private var popUp: Animation? = null

    fun getDropImageAnimation(v: View): Animation? {
        if (dropImage == null) {
            dropImage = AnimationUtils.loadAnimation(v.context, R.anim.drop_image)
        }
        return dropImage
    }

    private fun getDropTitleAnimation(v: View): Animation? {
        if (dropTitle == null) {
            dropTitle = AnimationUtils.loadAnimation(v.context, R.anim.drop_title)
        }
        return dropTitle
    }

    private fun getPopUpAnimation(v: View): Animation? {
        if (popUp == null) {
            popUp = AnimationUtils.loadAnimation(v.context, R.anim.popup)
        }
        return popUp
    }

    fun setDropTitleAnimation(v: TextView?) {
        v?.startAnimation(getDropTitleAnimation(v))
    }

    fun setPopUpAnimation(v: Button?) {
        v?.startAnimation(getPopUpAnimation(v))
    }
}