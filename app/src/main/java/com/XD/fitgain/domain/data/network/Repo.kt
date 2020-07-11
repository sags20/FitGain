package com.XD.fitgain.domain.data.network

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class Repo {
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    //Firestore
    fun getBusinessList(nombreCategoria: String): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Negocios")
            .whereEqualTo("categoria", nombreCategoria)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
    }

    fun getPromoList(negocioUid: String): Task<QuerySnapshot> {
        Log.d("PROMO_FETCH", negocioUid)
        return firebaseFirestore
            .collection("Promociones")
            .whereEqualTo("negocioUid", negocioUid.trim())
            .get()
    }
}