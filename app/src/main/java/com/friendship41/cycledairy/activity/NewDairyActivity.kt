package com.friendship41.cycledairy.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_new_dairy.*
import java.util.logging.Logger

const val SELECT_PLACE_REQUEST_CODE = 100
const val SELECT_PLACE_RESULT_CODE_SUCCESS = 200

class NewDairyActivity : AppCompatActivity() {
    private val log = Logger.getLogger(NewDairyActivity::class.java.name)
    private var selectedNowPoi: ParcelablePOI? = null

    private val tMapView = TMapView(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_dairy)


        // TODO: ok 버튼
        tv_new_dairy_ok.setOnClickListener {
            log.info("now: $selectedNowPoi")
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
            log.info("현재위치 검색 -> ${edt_new_dairy_place_now.text}")
            Thread(SearchService(edt_new_dairy_place_now.text.toString(), tMapData, this)).start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PLACE_REQUEST_CODE) {
            if (resultCode == SELECT_PLACE_RESULT_CODE_SUCCESS) {
                val poi = data?.getParcelableExtra<ParcelablePOI>("selectedPOI")
                if (poi == null) {
                    log.warning("잘못된 값이 돌아옴: $poi")
                    return
                }
                log.info("선택된 poi: $poi")
                edt_new_dairy_place_now.setText(poi.name)
                selectedNowPoi = poi
                setPositionToTMapView(tMapView, poi)
            } else {
                log.info("잘못된 response -> requestCode: ${requestCode}, resultCode: ${resultCode}, data: $data")
            }
        } else {
            log.warning("뭔가 잘못됨 requestCode: ${requestCode}, resultCode: ${resultCode}, data: $data")
        }
    }
}

class SearchService constructor(private val searchWord: String, private val tMapData: TMapData, private val context: AppCompatActivity) : Runnable {
    override fun run() {
        val poiList: ArrayList<ParcelablePOI> = ArrayList()
        tMapData.findTitlePOI(searchWord).map {
            ParcelablePOI(it.id, it.name, it.noorLat , it.noorLon)
        }.toCollection(poiList)

        val selectPlaceIntent = Intent(context, SelectPlaceActivity::class.java)
        selectPlaceIntent.putParcelableArrayListExtra("poiList", poiList)
        context.startActivityForResult(selectPlaceIntent, SELECT_PLACE_REQUEST_CODE)
    }
}

fun setPositionToTMapView(tMapView: TMapView, poi: ParcelablePOI) {

}

@Parcelize
data class ParcelablePOI(
    val id: String,
    val name: String,
    val lat: String,
    val lon: String
) : Parcelable

