package com.XD.fitgain.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogInBinding
import com.XD.fitgain.databinding.FragmentProfileBinding
import com.XD.fitgain.domain.data.network.Repo
import com.XD.fitgain.model.Busines
import com.XD.fitgain.views.NavigationContainerHome
import com.XD.fitgain.views.SplashScreen
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.category_recycler_style.view.*
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var binding: FragmentProfileBinding

    private lateinit var currentUser: com.XD.fitgain.model.User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        getUserData()


        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, SplashScreen::class.java))
        }

        // Inflate the layout for this fragment
        return binding.root

    }

    private fun setViewData() {
        Glide.with(this).load(currentUser.photoUrl).into(binding.circleImageView)
        binding.etAltura.setText("${currentUser.height}")
        binding.etCorreo.setText(currentUser.email)
        binding.etEdad.setText("${currentUser.edad}")
        binding.etNombreUsuario.setText(currentUser.nombre)
        binding.etPeso.setText("${currentUser.weight}")


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
