package com.XD.fitgain.views.fragments

import android.content.*
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.XD.fitgain.R
import kotlinx.android.synthetic.main.fragment_activity.*


class Activity : Fragment() {
    private var currentSteps = 0
    private var previousSteps = 0
    private var totalSteps = 0;

    private val stepsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                currentSteps = intent.extras?.get("steps").toString().toInt()
                loadData()

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(stepsReceiver, getIntentFilter())
        loadData()

        //Log.d("STEPS_DEBUG", sharedPreferences.getInt("stepCount", 0).toString())
        //Log.d("STEPS_DEBUG", "Pasos anteriores $previousSteps")
        //Log.d("STEPS_DEBUG", "Pasos actuales $currentSteps")

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(stepsReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }

    private fun saveData() {
        var sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("myPref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putInt("stepCount", totalSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("myPref", MODE_PRIVATE)
        previousSteps = sharedPreferences.getInt("stepCount", 0)
        totalSteps = previousSteps + currentSteps

        tv_steps.text = totalSteps.toString()
        progress_circular.apply {
            setProgressWithAnimation(totalSteps.toFloat())
        }
    }

    private fun getIntentFilter(): IntentFilter {
        val iFilter = IntentFilter()
        iFilter.addAction("STEPS_CHANGED")
        return iFilter
    }
}
