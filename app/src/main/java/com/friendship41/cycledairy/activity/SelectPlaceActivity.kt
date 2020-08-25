package com.friendship41.cycledairy.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.ArrayAdapter
import com.friendship41.cycledairy.R
import com.skt.Tmap.TMapPOIItem
import kotlinx.android.synthetic.main.activity_select_place.*

class SelectPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_place)

        val bundle: Bundle? = intent.extras
        val poiList = intent.getParcelableArrayListExtra<ParcelablePOI>("poiList")

        lv_select_place_poi.adapter = poiList?.let {
            ArrayAdapter<ParcelablePOI>(
                this,
                android.R.layout.simple_list_item_1,
                it
            )
        }

        lv_select_place_poi.setOnItemClickListener { parent, view, position, id ->
            intent.putExtra("selectedPOI", lv_select_place_poi.adapter.getItem(position) as ParcelablePOI)
            setResult(SELECT_PLACE_RESULT_CODE_SUCCESS, intent)
            finish()
        }
    }
}
