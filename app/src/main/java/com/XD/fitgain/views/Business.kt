package com.XD.fitgain.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.XD.fitgain.R
import com.XD.fitgain.adapters.RecyclerBusinesAdapter
import com.XD.fitgain.adapters.RecyclerPromoAdapter
import com.XD.fitgain.databinding.ActivityBusinessBinding
import com.XD.fitgain.databinding.ActivityDiscoverSelectedBinding
import com.XD.fitgain.domain.data.network.Repo
import com.XD.fitgain.model.Busines
import com.XD.fitgain.model.Promo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_business.*
import kotlinx.android.synthetic.main.activity_business.tv_categoria
import kotlinx.android.synthetic.main.activity_discover_selected.*

private val TAG: String = "PROMO_DEBUG"
class Business : AppCompatActivity(), RecyclerPromoAdapter.OnItemClickListener {
    private lateinit var binding: ActivityBusinessBinding

    private lateinit var busines: Busines
    private val firebaseRepo: Repo = Repo()

    private var promoList: List<Promo> = ArrayList()
    private val promoAdapter: RecyclerPromoAdapter = RecyclerPromoAdapter(promoList,this)

    private lateinit var negocioSeleccionadoUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusinessBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnBackScreen.setOnClickListener {
            finish()
        }

        getDataFromIntent()

        //Load data from firestore
        loadData()

        //Init recycler view
        recyclerViewPromo.layoutManager = LinearLayoutManager(this)
        recyclerViewPromo.adapter = promoAdapter
    }

    private fun getDataFromIntent() {
        busines = intent.getParcelableExtra("Busines") as Busines
        setDataFromIntent(busines)
    }

    private fun setDataFromIntent(busines: Busines) {
        Glide.with(this).load(busines.photoUrl).into(photo)
        tv_categoria.text = busines.categoria
        tv_restaurante.text = "${busines.nombre} - Promos"

        negocioSeleccionadoUid = busines.uid
    }

    override fun onItemClick(promo: Promo) {
        TODO("Not yet implemented")
    }

    private fun loadData() {
        promo_shimmer.startShimmer()
        firebaseRepo.getPromoList(negocioSeleccionadoUid).addOnCompleteListener {
            if (it.isSuccessful) {
                promo_shimmer.stopShimmer()
                promo_shimmer.visibility = View.GONE
                promoList = it.result!!.toObjects(Promo::class.java)
                promoAdapter.promoListItems = promoList
                promoAdapter.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Error: ${it.exception!!.message}")
            }
        }
    }
}