package com.friendship41.cycledairy.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapMarkerItem
import com.skt.Tmap.TMapPoint
import com.skt.Tmap.TMapView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_new_dairy.*
import java.util.logging.Logger

const val SELECT_PLACE_NOW_REQUEST_CODE = 100
const val SELECT_PLACE_TO_REQUEST_CODE = 101
const val RESULT_CODE_SUCCESS = 200

class NewDairyActivity : AppCompatActivity() {
    private val log = Logger.getLogger(NewDairyActivity::class.java.name)

    private lateinit var tMapView: TMapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_dairy)

        tMapView = TMapView(this)

        // TODO: ok 버튼
        tv_new_dairy_ok.setOnClickListener {
        }

        // cancel 버튼
        tv_new_dairy_cancel.setOnClickListener{
            finish()
        }

        // 맵 뷰
        tMapView.setSKTMapApiKey("l7xx575320139307413f9dd8196bf0803245")
        new_dairy_map_view.addView(tMapView)


        // tMap Data
        val tMapData = TMapData()

        // 현재위치 검색
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
                    edt_new_dairy_place_now
                )
            }
            SELECT_PLACE_TO_REQUEST_CODE -> {
                this.addMarkerItemToView(
                    "pinTo",
                    tMapView,
                    data?.getParcelableExtra("selectedPOI"),
                    edt_new_dairy_place_to)
            }
            else -> log.info("잘못된 requestCode -> requestCode: ${requestCode}, resultCode: ${resultCode}, data: $data")
        }
    }

    private fun addMarkerItemToView(markerId: String, tMapView: TMapView, poi: ParcelablePOI?) {
        if (poi == null) {
            log.warning("잘못된 poi 값이 들아옴")
            return
        }
        log.info("선택된 poi: $poi")
        tMapView.removeMarkerItem(markerId)
        tMapView.addMarkerItem(markerId, getTMapMarker(this, poi))
        tMapView.setCenterPoint(poi.lon.toDouble(), poi.lat.toDouble())
    }
    private fun addMarkerItemToView(markerId: String, tMapView: TMapView, poi: ParcelablePOI?, editText: EditText) {
        addMarkerItemToView(markerId, tMapView, poi)
        editText.setText(poi?.name ?: "")
    }
}

class SearchService constructor(
    private val searchWord: String,
    private val tMapData: TMapData,
    private val context: AppCompatActivity,
    private val requestCode: Int) : Runnable {
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

fun getTMapMarker(context: AppCompatActivity, poi: ParcelablePOI): TMapMarkerItem {
    val markerItem = TMapMarkerItem()
    val option = BitmapFactory.Options()
    option.inSampleSize = 12
    markerItem.icon = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.map_icon_red_305x400,
        option)
    markerItem.setPosition(0.5f, 0.5f)
    markerItem.tMapPoint = TMapPoint(poi.lat.toDouble(), poi.lon.toDouble())
    markerItem.name = poi.name
    return markerItem
}

@Parcelize
data class ParcelablePOI(
    val id: String,
    val name: String,
    val lat: String,
    val lon: String
) : Parcelable

