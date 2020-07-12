package com.XD.fitgain.views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class DiscoverSelected : AppCompatActivity(), RecyclerBusinesAdapter.OnItemClickListener {
    private lateinit var binding: ActivityDiscoverSelectedBinding

    private val firebaseRepo: Repo = Repo()

    private var businesList: List<Busines> = ArrayList()
    private val businesAdapter: RecyclerBusinesAdapter = RecyclerBusinesAdapter(businesList,this)

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

        //Search bar
        binding.etSearchPersons.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = binding.etSearchPersons.text.toString().trim()

                performSearch(searchText)
            }

        })
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
        firebaseRepo.getBusinessList(categoriaSeleccionada).addOnCompleteListener {
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

    private fun performSearch(searchParam: String) {
        categoria_shimmer.startShimmer()
        firebaseRepo.getBusinessList(categoriaSeleccionada).addOnCompleteListener {
            if (it.isSuccessful) {
                categoria_shimmer.stopShimmer()
                categoria_shimmer.visibility = View.GONE
                businesList = performFilter(searchParam, it.result!!.toObjects(Busines::class.java))
                businesAdapter.postListItems = businesList
                businesAdapter.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Error: ${it.exception!!.message}")
            }
        }
    }

    private fun performFilter(
        searchParam: String,
        result: MutableList<Busines>
    ): List<Busines> {
        var filterBusinesList = mutableListOf<Busines>()
        val pattern = searchParam.toRegex()
        for (b in result) {
            if (pattern.containsMatchIn(b.nombre)) {
                filterBusinesList.add(b)
            }
        }

        return filterBusinesList
    }

    override fun onItemClick(busines: Busines) {
        val intent = Intent(this, Business::class.java)
        intent.putExtra("Busines", busines)
        startActivity(intent)
    }
}
