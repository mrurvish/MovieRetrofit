package com.example.movieretrofit.fragments.ui.accountSettingsFragment

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.movieretrofit.firebase.Firebase
import com.example.movieretrofit.R
import com.example.movieretrofit.charts.PieCharts
import com.example.movieretrofit.data.Diet
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.slider.RangeSlider

class ChooseDietAlertDialog(val context: Context, val firebase: Firebase) {
    // Dialog to choose a diet
    private lateinit var alertDialog: AlertDialog
    private lateinit var dietView: View

    // Range slider for custom diet
    private lateinit var rangeSlider: RangeSlider
    private var diet: Diet = Diet()

    // Predefined diet plans
    private val standardDiet = Diet("Standard", 30f, 30f, 40f)
    private val ketoDiet = Diet("Keto", 20f, 75f, 5f)
    private val proteinDiet = Diet("Protein", 50f, 20f, 30f)

    private lateinit var standardLinLay: LinearLayout
    private lateinit var ketoLinLay: LinearLayout
    private lateinit var proteinLinLay: LinearLayout
    private lateinit var customLinLay: LinearLayout

    private lateinit var standardChart: PieChart
    private lateinit var ketoChart: PieChart
    private lateinit var proteinChart: PieChart
    private lateinit var customChart: PieChart

    // Function to create the dialog
    fun create(): ChooseDietAlertDialog {
        val layoutInflater = LayoutInflater.from(context)
        dietView = layoutInflater.inflate(R.layout.choose_diet_layout, null)
        initView(dietView)

        alertDialog = AlertDialog.Builder(context).create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.rounded_background)
        alertDialog.setView(dietView)

        // Button click listener to save the diet
        dietView.findViewById<Button>(R.id.send_diet_to_firebase).setOnClickListener {
            if (saveDiet()) alertDialog.dismiss()
        }

        return this
    }

    // Function to show the dialog
    fun show() = alertDialog.show()

    // Function to initialize the views
    private fun initView(dietView: View) {
        rangeSlider = dietView.findViewById(R.id.slider)
        createRangeSlider()

        standardLinLay = dietView.findViewById(R.id.diet_standart)
        ketoLinLay = dietView.findViewById(R.id.diet_keto)
        proteinLinLay = dietView.findViewById(R.id.diet_protein)
        customLinLay = dietView.findViewById(R.id.diet_custom)
        chooseDiet()

        standardChart = dietView.findViewById(R.id.diet_standard_Chart)
        ketoChart = dietView.findViewById(R.id.diet_keto_Chart)
        proteinChart = dietView.findViewById(R.id.diet_protein_Chart)
        customChart = dietView.findViewById(R.id.diet_custom_Chart)
        setPieCharts()
    }

    // Function to set up the pie charts
    private fun setPieCharts() {
        PieCharts(context, standardChart).setPieChart(standardDiet)
        PieCharts(context, ketoChart).setPieChart(ketoDiet)
        PieCharts(context, proteinChart).setPieChart(proteinDiet)
        firebase.getUserDietFromFirebase {
            PieCharts(context, customChart).setPieChart(firebase.diet)
        }
    }

    // Function to choose a diet plan
    private fun chooseDiet() {
        val back = AppCompatResources.getDrawable(context, R.drawable.selected_diet_back)
        standardLinLay.setOnClickListener {
            rangeSlider.visibility = View.GONE
            diet = standardDiet
            changeDietBackground(standard = back)
        }
        ketoLinLay.setOnClickListener {
            rangeSlider.visibility = View.GONE
            diet = ketoDiet
            changeDietBackground(keto = back)
        }
        proteinLinLay.setOnClickListener {
            rangeSlider.visibility = View.GONE
            diet = proteinDiet
            changeDietBackground(protein = back)
        }
        customLinLay.setOnClickListener {
            rangeSlider.visibility = View.VISIBLE
            changeDietBackground(custom = back)
        }
    }

    // Function to change the background based on the selected diet
    private fun changeDietBackground(
        standard: Drawable? = null,
        keto: Drawable? = null,
        protein: Drawable? = null,
        custom: Drawable? = null
    ) {
        standardLinLay.background = standard
        ketoLinLay.background = keto
        proteinLinLay.background = protein
        customLinLay.background = custom
    }

    // Function to save the selected diet to Firebase
    private fun saveDiet(): Boolean {
        return if (diet.proteinCf == 0f || diet.fatCf == 0f || diet.carbsCf == 0f) {
            Toast.makeText(context, "Percentage cannot be 0%!", Toast.LENGTH_SHORT).show()
            false
        } else {
            firebase.sendUserDietToFirebase(diet)
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            true
        }
    }

    // Function to create and configure the range slider for custom diet
    private fun createRangeSlider() {
        val green = ColorStateList.valueOf(context.getColor(R.color.green))
        val liteGreen = ColorStateList.valueOf(context.getColor(R.color.light_green))

        rangeSlider.trackActiveTintList = green
        rangeSlider.trackInactiveTintList = liteGreen
        rangeSlider.thumbTintList = green

        rangeSlider.valueFrom = 0f
        rangeSlider.valueTo = 100f
        rangeSlider.stepSize = 5f

        firebase.getUserDietFromFirebase {
            rangeSlider.values = listOf(it.proteinCf, it.proteinCf + it.fatCf)
        }
        rangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values // Get the current values
            val proteinCf = values.first()
            val fatCf = values.last() - values.first()
            val carbCf = (100 - values.last())

            diet = Diet("Custom", proteinCf, fatCf, carbCf)
            PieCharts(context, customChart).setPieChart(diet) // Update
        }
    }

//    private fun updateTvCf(diet: Diet) {
//        val proteinText = "${context.getString(R.string.protein)} ${diet.proteinCf}%"
//        val fatText = "${context.getString(R.string.fat)} ${diet.fatCf}%"
//        val carbText = "${context.getString(R.string.carb)} ${diet.carbsCf}%"
//        tvProtein.text = proteinText
//        tvFat.text = fatText
//        tvCarb.text = carbText
//    }

}