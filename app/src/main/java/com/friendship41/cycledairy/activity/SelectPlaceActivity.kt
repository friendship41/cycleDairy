package com.friendship41.cycledairy.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R
import kotlinx.android.synthetic.main.activity_select_place.*

class SelectPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_place)

        val bundle: Bundle? = intent.extras
        val poiList = intent.getParcelableArrayListExtra<ParcelablePOI>("poiList")
        val stringList = poiList?.map {
            it.name
        }?.toList()

        lv_select_place_poi.adapter = stringList?.let {
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                it
            )
        }

        lv_select_place_poi.setOnItemClickListener { _, _, position, _ ->
            intent.putExtra("selectedPOI", poiList?.get(position) as ParcelablePOI)
            setResult(SELECT_PLACE_RESULT_CODE_SUCCESS, intent)
            finish()
        }
    }
}
