package com.hyunki.bard2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
        val syllable:String?,
        val note:String?,
        val rawNote:Int = 0,
        val duration: Int = 0
):Parcelable
