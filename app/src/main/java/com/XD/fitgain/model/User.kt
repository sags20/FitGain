package com.XD.fitgain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var uid: String = "",
    var nombre: String = "",
    var edad: Int = 0,
    var email: String = "",
    var genero: String = "",
    var height: Double = 0.0,
    var photoUrl: String = "",
    var points: Int = 0,
    var weight: Double = 0.0
): Parcelable