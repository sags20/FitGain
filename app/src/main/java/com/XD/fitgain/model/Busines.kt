package com.XD.fitgain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Busines(
    val uid: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val photoUrl: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
): Parcelable