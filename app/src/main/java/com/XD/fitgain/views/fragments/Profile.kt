package com.XD.fitgain.views.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

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
import com.google.firebase.storage.FirebaseStorage
import com.tapadoo.alerter.Alerter
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.category_recycler_style.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class Profile : Fragment() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var binding: FragmentProfileBinding

    private lateinit var currentUser: com.XD.fitgain.model.User
    lateinit var alertDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        alertDialog = SpotsDialog.Builder().setContext(activity).build()
        getUserData()


        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, SplashScreen::class.java))
        }

        binding.circleImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.btnSaveChangeUserAccount.setOnClickListener {
            currentUser.nombre = binding.etNombreUsuario.text.toString()
            currentUser.weight = binding.etPeso.text.toString().toDouble()
            currentUser.height = binding.etAltura.text.toString().toDouble()
            currentUser.edad = binding.etEdad.text.toString().toInt()
            currentUser.goalStep = binding.etMetapasos.text.toString().toInt()
            uploadImageToFirebaseStorage()
        }

        // Inflate the layout for this fragment
        return binding.root

    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proced to show preview photo selected
            selectedPhotoUri = data.data

            Glide.with(requireActivity()).load(selectedPhotoUri).into(binding.circleImageView)
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) {
            saveUser()
            return
        }
        alertDialog.show()

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("PROFILE_DEBUG", "Succesfully upload image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("REGISTER_DEBUG", "File location: ${it}")

                    currentUser.photoUrl = it.toString()
                    saveUser()
                    alertDialog.dismiss()
                    selectedPhotoUri = null
                }
            }
            .addOnFailureListener {
                Alerter.create(activity)
                    .setText("Algo salió mal :(")
                    .setBackgroundColorRes(R.color.alert_default_error_background)
                    .show()
            }
    }

    private fun saveUser() {
        firebaseFirestore.collection("Usuarios").document(currentUser.uid).set(currentUser)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Alerter.create(activity)
                        .setText("¡Datos actualizados correctamente!")
                        .setBackgroundColorRes(R.color.alerter_default_success_background)
                        .show()
                } else {
                    Alerter.create(activity)
                        .setText("Ha ocurrido un error")
                        .setBackgroundColorRes(R.color.alert_default_error_background)
                        .show()
                }
            }
    }

    private fun setViewData() {
        Glide.with(this).load(currentUser.photoUrl).into(binding.circleImageView)
        binding.etAltura.setText("${currentUser.height}")
        binding.etCorreo.setText(currentUser.email)
        binding.etEdad.setText("${currentUser.edad}")
        binding.etNombreUsuario.setText(currentUser.nombre)
        binding.etPeso.setText("${currentUser.weight}")
        binding.etMetapasos.setText(currentUser.goalStep.toString())

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
