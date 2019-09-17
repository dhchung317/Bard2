package com.hyunki.bard2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
        val rawNote:Int = 0,
        val syllable:String?,
        val duration: Int = 0,
        val note:String?

):Parcelable
