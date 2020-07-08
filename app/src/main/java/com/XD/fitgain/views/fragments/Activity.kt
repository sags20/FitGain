package com.XD.fitgain.views.fragments

import android.R
import android.app.AlertDialog
import android.content.*
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.XD.fitgain.adapters.AlertDialogUtility
import com.XD.fitgain.databinding.FragmentActivityBinding
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlin.math.pow


class Activity : Fragment() {
    private lateinit var binding: FragmentActivityBinding

    private var currentSteps = 0
    private var previousSteps = 0
    private var totalSteps = 0
    private var totalDistance = 0.0

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
        binding = FragmentActivityBinding.inflate(inflater, container, false)

        binding.btnInfo.setOnClickListener {
            AlertDialogUtility.alertDialog(
                requireContext(),
                getString(com.XD.fitgain.R.string.oms_info),
                2
            )
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(stepsReceiver, getIntentFilter())
        loadData()
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
            requireActivity().getSharedPreferences("FitnessData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putInt("stepCount", totalSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("FitnessData", MODE_PRIVATE)
        previousSteps = sharedPreferences.getInt("stepCount", 0)
        totalSteps = previousSteps + currentSteps

        //Distance setting values
        totalDistance = (totalSteps * 0.71777203560149) / 1000
        tv_disValue.text = String.format("%.2f", totalDistance) + " Km"
        pgb_distance.apply {
            setProgressWithAnimation(totalDistance.toFloat())
        }

        //Steps setting values
        tv_steps.text = totalSteps.toString()
        progress_circular.apply {
            setProgressWithAnimation(totalSteps.toFloat())
        }

        //Goal setting value
        var goal = 2500
        tv_percentage_steps.text = String.format(
            "%.2f",
            (totalSteps.toFloat() / goal.toFloat()) * 100
        ) + " % de tu meta diaria"

        //Calories Burned
        var weight = 90.0 //kg
        var height = 1.82.pow(2) //m al cuadrado
        var bmi = weight / height

        var age = 16
        var speedFactor = 3.0 //average for walk

        var caloriesBurned = (totalSteps * 0.04 * bmi * age * speedFactor) / 1000

        tv_calValue.text = String.format(
            "%.2f",
            caloriesBurned
        ) + " Cal"

        pgb_calories.apply {
            setProgressWithAnimation(caloriesBurned.toFloat())
        }

        //Points conversion
        val goalPoints = 10
        var points = 0.001 * totalSteps
        tv_poinstValue.text = String.format(
            "%.2f",
            points
        ) + " PS"

        tv_pointsInfo.text = String.format(
            "%.0f",
            points
        )

        pgb_limitedPoints.apply {
            setProgressWithAnimation(points.toFloat())
        }

    }

    private fun getIntentFilter(): IntentFilter {
        val iFilter = IntentFilter()
        iFilter.addAction("STEPS_CHANGED")
        return iFilter
    }
}
