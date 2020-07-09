package com.XD.fitgain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Promo(
    val titulo: String = "",
    val descripcion: String = "",
    val photoUrl: String = "",
    val negocioUid: String = "",
    val pointsRequired: Int = 0,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
): Parcelable