package com.XD.fitgain.domain.data.network

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class Repo {
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //Auth

    //Firestore
    fun getPostList(nombreCategoria: String): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("Negocios")
            .whereEqualTo("categoria",nombreCategoria)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
    }
}