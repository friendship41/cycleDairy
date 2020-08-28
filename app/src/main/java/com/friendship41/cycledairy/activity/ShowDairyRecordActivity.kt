package com.friendship41.cycledairy.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R
import com.friendship41.cycledairy.data.DairyRecord
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapPoint
import com.skt.Tmap.TMapView
import kotlinx.android.synthetic.main.activity_show_dairy_record.*

class ShowDairyRecordActivity : AppCompatActivity() {
    private lateinit var tMapView: TMapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_dairy_record)

        // T-Map 연동
        tMapView = TMapView(this)
        tMapView.setSKTMapApiKey("l7xx575320139307413f9dd8196bf0803245")
        show_dairy_record_map_view.addView(tMapView)

        intent.extras
        val dairyRecord = intent.getParcelableExtra<DairyRecord>("dairyRecord")
        Thread(DrawPolyLineRunnable(
            this,
            tMapView,
            dairyRecord?.dairyRecordSeq ?: 0,
            dairyRecord?.startLocationName ?: "startName",
            dairyRecord?.startLocationLat ?: 0.0,
            dairyRecord?.startLocationLon ?: 0.0,
            dairyRecord?.endLocationName ?: "endPin",
            dairyRecord?.endLocationLat ?: 0.0,
            dairyRecord?.endLocationLon ?: 0.0
        )).start()

        // cancel 버튼
        tv_show_dairy_cancel.setOnClickListener {
            finish()
        }
    }
}

class DrawPolyLineRunnable(private var context: AppCompatActivity,
                           private val tMapView: TMapView,
                           private val id: Int,
                           private val startName: String,
                           private val startLat: Double,
                           private val startLon: Double,
                           private val endName: String,
                           private val endLat: Double,
                           private val endLon: Double): Runnable {

    override fun run() {
        val tMapPointStart = TMapPoint(startLat, startLon)
        val tMapPointEnd = TMapPoint(endLat, endLon)
        val tMapPolyLine = TMapData().findPathData(tMapPointStart, tMapPointEnd)
        tMapPolyLine.lineColor = Color.RED
        tMapPolyLine.lineWidth = 15f
        tMapView.addTMapPolyLine("line", tMapPolyLine)
        tMapView.addMarkerItem("pinFrom", getTMapMarker(context,
                ParcelablePOI("$id", startName, "$startLat", "$startLon"),
            R.drawable.map_pin_red_icon,
            3))
        tMapView.addMarkerItem("pinTo", getTMapMarker(context,
            ParcelablePOI("$id", endName, "$endLat", "$endLon"),
            R.drawable.map_pin_blue_icon,
            3))
        tMapView.setCenterPoint(startLon, startLat)
    }
}
