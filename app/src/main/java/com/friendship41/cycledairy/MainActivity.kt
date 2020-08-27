package com.friendship41.cycledairy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.friendship41.cycledairy.activity.ListDairyActivity
import com.friendship41.cycledairy.activity.NewDairyActivity
import com.friendship41.cycledairy.activity.ParcelablePOI
import com.friendship41.cycledairy.activity.getTMapMarker
import com.friendship41.cycledairy.common.REQUEST_PERMISSION_ACCESS_FINE_LOCATION
import com.skt.Tmap.TMapView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
//    private val log = Logger.getLogger(MainActivity::class.java.name)

    private lateinit var tMapView: TMapView
    private var locationManager: LocationManager? = null
    private var locationNow: Location? = null
    private var locationPermission = PackageManager.PERMISSION_DENIED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // T-Map 연동
        tMapView = TMapView(this)
        tMapView.setSKTMapApiKey("l7xx575320139307413f9dd8196bf0803245")
        main_map_view.addView(tMapView)

        // 위치권한 체크 및 현재위치 불러와서 세팅
        locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission == PackageManager.PERMISSION_GRANTED) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000L,
                10f,
                locationListener
            )
            locationNow = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (locationNow != null) {
                tMapView.setCenterPoint(locationNow!!.longitude, locationNow!!.latitude)
            }
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_ACCESS_FINE_LOCATION
                )
            }
        }


        // 리스트 보기 버튼(tv)
        tv_main_list.setOnClickListener {
            val listDairyIntent = Intent(this, ListDairyActivity::class.java)
            startActivity(listDairyIntent)
        }
        // 작성 페이지 버튼(tv)
        tv_main_new.setOnClickListener {
            val newDairyIntent = Intent(this, NewDairyActivity::class.java)
            startActivity(newDairyIntent)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    locationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        1000L,
                        10f,
                        locationListener
                    )
                    locationNow = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (locationNow != null) {
                        tMapView.setCenterPoint(locationNow!!.longitude, locationNow!!.latitude)
                        tMapView.removeMarkerItem("currentLocation")
                        tMapView.addMarkerItem("currentLocation", getTMapMarker(
                            this,
                            ParcelablePOI(
                                "main",
                                "currentLoction",
                                "${locationNow!!.latitude}",
                                "${locationNow!!.longitude}"),
                            R.drawable.current_location_icon))
                    }
                }
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationNow = location
            if (locationNow != null) {
                tMapView.setCenterPoint(locationNow!!.longitude, locationNow!!.latitude)
                tMapView.getMarkerItemFromID("currentLocation").latitude = locationNow!!.latitude
                tMapView.getMarkerItemFromID("currentLocation").longitude = locationNow!!.longitude
            }
        }
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
}
