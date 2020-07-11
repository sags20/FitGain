package com.XD.fitgain.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.XD.fitgain.R
import com.XD.fitgain.databinding.FragmentLogInBinding
import com.XD.fitgain.databinding.FragmentProfileBinding
import com.XD.fitgain.views.NavigationContainerHome
import com.XD.fitgain.views.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    private var auth: FirebaseAuth =  FirebaseAuth.getInstance()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        binding.btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, SplashScreen::class.java))
        }

        // Inflate the layout for this fragment
        return binding.root


    }

}
