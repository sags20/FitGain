package com.XD.fitgain.views

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.XD.fitgain.adapters.RecyclerBusinesAdapter
import com.XD.fitgain.databinding.ActivityDiscoverSelectedBinding
import com.XD.fitgain.domain.data.network.Repo
import com.XD.fitgain.model.Busines
import kotlinx.android.synthetic.main.activity_discover_selected.*


private val TAG: String = "DISCOVER_DEBUG"

class DiscoverSelected : AppCompatActivity() {
    private lateinit var binding: ActivityDiscoverSelectedBinding

    private val firebaseRepo: Repo = Repo()

    private var businesList: List<Busines> = ArrayList()
    private val businesAdapter: RecyclerBusinesAdapter = RecyclerBusinesAdapter(businesList)

    private lateinit var categoriaSeleccionada: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscoverSelectedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnBackScreen.setOnClickListener {
            finish()
        }

        //Get data from intent
        getDataFromIntent()

        //Load data from Firebase Firestore
        loadPostData()


        //Init recycler view
        recyclerViewBussines.layoutManager = LinearLayoutManager(this)
        recyclerViewBussines.adapter = businesAdapter
    }

    private fun getDataFromIntent() {
        val iin = intent
        val b = iin.extras

        if (b != null) {
            val j = b["categoria"] as String?
            tv_categoria.text = j
            categoriaSeleccionada = j!!
        }
    }

    private fun loadPostData() {
        categoria_shimmer.startShimmer()
        firebaseRepo.getPostList(categoriaSeleccionada).addOnCompleteListener {
            if (it.isSuccessful) {
                categoria_shimmer.stopShimmer()
                categoria_shimmer.visibility = View.GONE
                businesList = it.result!!.toObjects(Busines::class.java)
                businesAdapter.postListItems = businesList
                businesAdapter.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Error: ${it.exception!!.message}")
            }
        }
    }
}
