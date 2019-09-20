package com.hyunki.bard2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
data class Song(
        val songTitle: String,
        val songNotes: List<Note>
):Parcelable{
}
