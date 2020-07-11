package com.XD.fitgain.views.fragments

import android.app.Activity
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
import com.XD.fitgain.databinding.FragmentRegister2Binding
import com.XD.fitgain.model.User
import com.bumptech.glide.Glide
import com.tapadoo.alerter.Alerter

class Register2 : Fragment() {
    private lateinit var binding: FragmentRegister2Binding

    private lateinit var nuevoUsuario: User
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegister2Binding.inflate(inflater, container, false)

        binding.cimgProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.uploadPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.btnContinuar.setOnClickListener {

            if (binding.etAge.text.isEmpty() || binding.etAltura.text.isEmpty() || binding.etPeso.text.isEmpty()) {
                Alerter.create(activity)
                    .setText("Completa todos los campos para brindarte una experiencia más personalizada.")
                    .setBackgroundColorRes(R.color.alert_default_error_background)
                    .show()
            } else {
                nuevoUsuario.edad = binding.etAge.text.toString().toInt()
                nuevoUsuario.weight = binding.etPeso.text.toString().toDouble()
                nuevoUsuario.height = binding.etAltura.text.toString().toDouble()

                //Passing the user to the next fragment
                var action = Register2Directions.actionRegister2ToRegister3(nuevoUsuario,password)
                it.findNavController().navigate(action)
            }




        }

        binding.btnBackScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_register2_to_register1)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            var args = Register2Args.fromBundle(requireArguments())
            nuevoUsuario = args.userPart1
            password = args.password
        }

    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proced to show preview photo selected
            selectedPhotoUri = data.data

            Glide.with(requireActivity()).load(selectedPhotoUri).into(binding.cimgProfilePicture)

            nuevoUsuario.photoUrl = selectedPhotoUri.toString()
        }
    }


}
