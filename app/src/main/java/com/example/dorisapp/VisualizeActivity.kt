package com.example.dorisapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.sinh

class VisualizeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualize)

        var x: Double = 0.0
        var y: Double = 0.0

        var graph : GraphView = findViewById(R.id.graph)
        var series : LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()

        x = -0.5

        for (i in 0..50)
        {
            x += 0.1
            y = sinh(x)
            series.appendData(DataPoint(x,y), true, 500)
        }

        graph.addSeries(series)


    }

   // val series: LineGraphSeries<DataPoint> = LineGraphSeries {DataPoint(0.1, 0.2)}


}