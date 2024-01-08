package com.example.movieretrofit.charts

import android.content.Context
import android.graphics.Color
import com.example.movieretrofit.R
import com.example.movieretrofit.data.Diet
import com.example.movieretrofit.data.Nutrients
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt


class BarCharts {
    fun setBarChart(context: Context, barChart: BarChart, nutrients: Nutrients, diet: Diet) {
        // Set text to display when chart has no data
        barChart.setNoDataText("Eat something!)")
        barChart.isDoubleTapToZoomEnabled = false
        barChart.setScaleEnabled(false)

        // Calculate balanced nutrients percentage based on the given diet
        val balancedNutrients = nutrients.getBalancedNutrientsInPercentage(diet)

        // Create BarEntries for proteins, fats, and carbohydrates
        val entries = arrayListOf(
            (BarEntry(3f, balancedNutrients.protein, "protein")),
            (BarEntry(2f, balancedNutrients.fat, "fat")),
            (BarEntry(1f, balancedNutrients.carb, "carbs"))
        )

        // Create BarDataSet with stack labels for proteins, fats, and carbohydrates
        val dataSet = BarDataSet(entries, "")
        dataSet.stackLabels = arrayOf("proteins", "fats", "carbohydrates")

        // Set colors for proteins, fats, and carbohydrates
        dataSet.colors = listOf(
            context.getColor(R.color.protein),
            context.getColor(R.color.fat),
            context.getColor(R.color.carb)
        )

        // Create BarData and format values to display percentage
        val data = BarData(dataSet)
        dataSet.valueTextColor = Color.BLACK
        data.barWidth = 0.8f
        data.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.roundToInt()}%"
            }
        })

        barChart.animateY(0)

        // Customize X-Axis
        val xAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        // Customize Left Axis
        val leftAxis = barChart.axisLeft
        leftAxis.setDrawLabels(false)
        leftAxis.axisMaximum = maxOf(
            balancedNutrients.protein,
            balancedNutrients.fat,
            balancedNutrients.carb
        ) + 4
        leftAxis.axisMinimum = 0f

        // Customize Right Axis
        val rightAxis = barChart.axisRight
        rightAxis.setDrawLabels(true)
        rightAxis.axisMaximum = maxOf(
            balancedNutrients.protein,
            balancedNutrients.fat,
            balancedNutrients.carb
        ) + 4
        rightAxis.axisMinimum = 0f

        // Disable legend and description
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.invalidate()

        // Customize X-Axis labels
        xAxis.apply {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = -90f
            setDrawLabels(true)
        }

        // Set custom labels for X-Axis
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value) {
                    3f -> "proteins"
                    2f -> "fats"
                    1f -> "Carbohydrates"
                    else -> ""
                }
            }
        }.apply {
            xAxis.setLabelCount(3, false)
        }

        // Set chart data and customize grid lines
        barChart.data = data
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        rightAxis.setDrawGridLines(false)

        // Format values for Right Axis
        rightAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.roundToInt()}%"
            }
        }

        // Add limit line for 100% on Left Axis
        val ll1 = LimitLine(100f)
        ll1.lineColor = context.getColor(R.color.red)
        ll1.lineWidth = 3f

        leftAxis.removeAllLimitLines()
        leftAxis.addLimitLine(ll1)
    }

}