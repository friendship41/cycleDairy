package com.friendship41.cycledairy.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.friendship41.cycledairy.R
import com.friendship41.cycledairy.common.REQUEST_PERMISSION_ACCESS_FINE_LOCATION
import com.friendship41.cycledairy.common.RESULT_CODE_SUCCESS
import com.friendship41.cycledairy.common.SELECT_PLACE_NOW_REQUEST_CODE
import com.friendship41.cycledairy.common.SELECT_PLACE_TO_REQUEST_CODE
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapMarkerItem
import com.skt.Tmap.TMapPoint
import com.skt.Tmap.TMapView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_new_dairy.*
import java.util.logging.Logger


class NewDairyActivity : AppCompatActivity() {
    private val log = Logger.getLogger(NewDairyActivity::class.java.name)

    private lateinit var tMapView: TMapView
    private var locationManager: LocationManager? = null
    private var locationNow: Location? = null
    private var locationPermission = PackageManager.PERMISSION_DENIED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_dairy)

        tMapView = TMapView(this)
        // 맵 뷰
        tMapView.setSKTMapApiKey("l7xx575320139307413f9dd8196bf0803245")
        new_dairy_map_view.addView(tMapView)

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
                    REQUEST_PERMISSION_ACCESS_FINE_LOCATION)
            }
        }


        // tMap Data
        val tMapData = TMapData()

        // TODO: ok 버튼
        tv_new_dairy_ok.setOnClickListener {
//            val pinNow = tMapView.getMarkerItemFromID("pinNow") ?: null
//            val pinTo = tMapView.getMarkerItemFromID("pinTo") ?: null
//
//            if (pinNow == null) {
//
//            }
//
//            val okIntent = Intent()
//            setResult(RESULT_CODE_SUCCESS)
        }

        // cancel 버튼
        tv_new_dairy_cancel.setOnClickListener{
            finish()
        }

        // 현재위치 검색버튼
        btn_new_dairy_place_now.setOnClickListener {
            if (edt_new_dairy_place_now.text.toString() == "") {
                Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            log.info("현재위치 검색 -> ${edt_new_dairy_place_now.text}")
            Thread(SearchService(edt_new_dairy_place_now.text.toString(),
                tMapData,
                this,
                SELECT_PLACE_NOW_REQUEST_CODE)).start()
        }

        // 목적지 검색
        btn_new_dairy_place_to.setOnClickListener {
            if (edt_new_dairy_place_to.text.toString() == "") {
                Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            log.info("목적지 검색 -> ${edt_new_dairy_place_to.text}")
            Thread(SearchService(edt_new_dairy_place_to.text.toString(),
                tMapData,
                this,
                SELECT_PLACE_TO_REQUEST_CODE)).start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CODE_SUCCESS) {
            log.warning("뭔가 잘못됨 requestCode: ${requestCode}, resultCode: ${resultCode}, data: $data")
            return
        }

        when (requestCode) {
            SELECT_PLACE_NOW_REQUEST_CODE -> {
                this.addMarkerItemToView(
                    "pinNow",
                    tMapView,
                    data?.getParcelableExtra("selectedPOI"),
                    edt_new_dairy_place_now,
                    R.drawable.map_pin_red_icon
                )
            }
            SELECT_PLACE_TO_REQUEST_CODE -> {
                this.addMarkerItemToView(
                    "pinTo",
                    tMapView,
                    data?.getParcelableExtra("selectedPOI"),
                    edt_new_dairy_place_to,
                    R.drawable.map_pin_blue_icon
                )
            }
            else -> log.info("잘못된 requestCode -> requestCode: ${requestCode}, resultCode: ${resultCode}, data: $data")
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
                    locationNow = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) as Location
                    if (locationNow != null) {
                        tMapView.setCenterPoint(locationNow!!.longitude, locationNow!!.latitude)
                    }
                }
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationNow = location
        }
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun addMarkerItemToView(markerId: String, tMapView: TMapView, poi: ParcelablePOI?, iconFileId: Int) {
        if (poi == null) {
            log.warning("잘못된 poi 값이 들아옴")
            return
        }
        log.info("선택된 poi: $poi")
        tMapView.removeMarkerItem(markerId)
        tMapView.addMarkerItem(markerId, getTMapMarker(this, poi, iconFileId))
        tMapView.setCenterPoint(poi.lon.toDouble(), poi.lat.toDouble())
    }
    private fun addMarkerItemToView(markerId: String, tMapView: TMapView, poi: ParcelablePOI?, editText: EditText, iconFileId: Int) {
        addMarkerItemToView(markerId, tMapView, poi, iconFileId)
        editText.setText(poi?.name ?: "")
    }
}

/**
 * 책임: Tmap의 검색통신을 담당
 */
class SearchService constructor(
    private val searchWord: String,
    private val tMapData: TMapData,
    private val context: AppCompatActivity,
    private val requestCode: Int) :  Runnable {
    override fun run() {
        val poiList: ArrayList<ParcelablePOI> = ArrayList()
        tMapData.findTitlePOI(searchWord)?.map {
            ParcelablePOI(it.id, it.name, it.noorLat , it.noorLon)
        }?.toCollection(poiList)

        if (poiList.size == 0) {
            return
        }

        val selectPlaceIntent = Intent(context, SelectPlaceActivity::class.java)
        selectPlaceIntent.putParcelableArrayListExtra("poiList", poiList)
        context.startActivityForResult(selectPlaceIntent, requestCode)
    }
}

/**
 * 기능: tMap 마커를 생성
 */
fun getTMapMarker(context: AppCompatActivity, poi: ParcelablePOI, iconFileId: Int): TMapMarkerItem {
    val markerItem = TMapMarkerItem()
    val option = BitmapFactory.Options()
    option.inSampleSize = 3
    markerItem.icon = BitmapFactory.decodeResource(
        context.resources,
        iconFileId,
        option)
    markerItem.setPosition(0.5f, 0.5f)
    markerItem.tMapPoint = TMapPoint(poi.lat.toDouble(), poi.lon.toDouble())
    markerItem.name = poi.name
    return markerItem
}

/**
 * activity 이동을 위한 Parcelable POI
 */
@Parcelize
data class ParcelablePOI(
    val id: String,
    val name: String,
    val lat: String,
    val lon: String
) : Parcelable

