package com.XD.fitgain.views.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.XD.fitgain.adapters.AlertDialogUtility

import com.XD.fitgain.databinding.FragmentDiscoverBinding
import com.XD.fitgain.domain.data.network.Repo
import com.XD.fitgain.model.Busines
import com.XD.fitgain.views.DiscoverSelected
import kotlinx.android.synthetic.main.activity_discover_selected.*
import kotlinx.android.synthetic.main.fragment_discover.*

private val TAG: String = "DISCOVER_FRAGMENT_DEBUG"

class Discover : Fragment() {

    private lateinit var binding: FragmentDiscoverBinding
    private val firebaseRepo: Repo = Repo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        binding.btnInfo.setOnClickListener {
            AlertDialogUtility.alertDialog(
                requireContext(),
                "Aquí puedes descubrir a donde gastar tus puntos de salud, podrás obtener productos completos, promociones, descuentos y más. Al ser una aplicación en fase BETA no es posible realizar un canjeo de puntos en la vida real, es decir que todo lo que se encuentre en esta pestaña es ficticio. Hecho con amor por el equipo de FitGain.",
                1
            )
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getCountRestaurante()
        getCountPet()
        getCountRetail()
        getCountTecnologia()
    }

    private fun getCountRestaurante() {
        firebaseRepo.getBusinessList("Restaurantes").addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result!!.toObjects(Busines::class.java).size == 0) {
                    tvCountRestaurante.text = "Proximamente..."
                } else {
                    tvCountRestaurante.text =
                        "${it.result!!.toObjects(Busines::class.java).size} Negocios"
                    binding.cardviewRestaurante.setOnClickListener {
                        startActivityForResult(
                            Intent(
                                requireActivity(),
                                DiscoverSelected::class.java
                            ).putExtra("categoria", "Restaurantes"), 777
                        )
                    }
                }

            } else {
                Log.d(TAG, "Error: ${it.exception!!.message}")
            }
        }
    }

    private fun getCountTecnologia() {
        firebaseRepo.getBusinessList("Tecnologia").addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result!!.toObjects(Busines::class.java).size == 0) {
                    tvCountTec.text = "Proximamente..."
                } else {
                    tvCountTec.text = "${it.result!!.toObjects(Busines::class.java).size} Negocios"
                    binding.cardTecnologia.setOnClickListener {
                        startActivityForResult(
                            Intent(
                                requireActivity(),
                                DiscoverSelected::class.java
                            ).putExtra("categoria", "Tecnologia"), 777
                        )
                    }
                }

            } else {
                Log.d(TAG, "Error: ${it.exception!!.message}")
            }
        }
    }

    private fun getCountPet() {
        firebaseRepo.getBusinessList("Pet").addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result!!.toObjects(Busines::class.java).size == 0) {
                    tvCountPet.text = "Proximamente..."
                } else {
                    tvCountPet.text = "${it.result!!.toObjects(Busines::class.java).size} Negocios"
                    binding.cardPetstore.setOnClickListener {
                        startActivityForResult(
                            Intent(
                                requireActivity(),
                                DiscoverSelected::class.java
                            ).putExtra("categoria", "Pet"), 777
                        )
                    }
                }

            } else {
                Log.d(TAG, "Error: ${it.exception!!.message}")
            }
        }
    }

    private fun getCountRetail() {
        firebaseRepo.getBusinessList("Retail").addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result!!.toObjects(Busines::class.java).size == 0) {
                    tvCountRetail.text = "Proximamente..."
                } else {
                    tvCountRetail.text =
                        "${it.result!!.toObjects(Busines::class.java).size} Negocios"
                    binding.cardRetail.setOnClickListener {
                        startActivityForResult(
                            Intent(
                                requireActivity(),
                                DiscoverSelected::class.java
                            ).putExtra("categoria", "Retail"), 777
                        )
                    }
                }
            } else {
                Log.d(TAG, "Error: ${it.exception!!.message}")
            }
        }
    }

}
