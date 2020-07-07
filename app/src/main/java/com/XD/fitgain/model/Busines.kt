package com.XD.fitgain.model

import com.google.firebase.Timestamp


data class Busines(
    val nombre: String = "",
    val categoria: String = "",
    val photoUrl: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)