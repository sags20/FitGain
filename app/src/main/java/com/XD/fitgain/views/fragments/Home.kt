package com.XD.fitgain.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.XD.fitgain.databinding.FragmentHomeBinding
import com.XD.fitgain.domain.data.network.Repo
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import kotlin.math.pow
import kotlin.math.roundToInt


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: com.XD.fitgain.model.User

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        getUserData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getUserData()
    }

    private fun setViewData() {
        Glide.with(this).load(currentUser.photoUrl).into(binding.imgProfile)
        binding.tvUsuario.text = "¡Hola! ${currentUser.nombre}"
        binding.tvPoints.text = currentUser.points.toString()
        binding.tvAltura.text = "${currentUser.height} cm"
        binding.tvPeso.text = "${currentUser.weight} kg"
        binding.tvMetapasos.text = currentUser.goalStep.toString()
        binding.pasos.text =
            (currentUser.weight / (currentUser.height / 100.0).pow(2)).roundToInt().toString()

        if (currentUser.weight == 1.0 || currentUser.height == 1.0 || currentUser.edad == 0) {
            binding.tvMessage.text = "Actualiza tu altura, peso y edad para una mayor presición en los cálculos de BMI y Calorías."
            binding.tvMessage.visibility = View.VISIBLE
        } else {
            binding.tvMessage.visibility = View.GONE
        }
    }

    private fun getUserData() {
        firebaseFirestore
            .collection("Usuarios")
            .document(auth.currentUser!!.uid.trim())
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.d("GET_USER_DATA", "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    currentUser = documentSnapshot.toObject(com.XD.fitgain.model.User::class.java)!!
                    setViewData()
                } else {
                    Log.d("GET_USER_DATA", "Current data: null")
                }
            }

    }


}
