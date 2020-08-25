package com.friendship41.cycledairy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R
import com.skt.Tmap.TMapView
import kotlinx.android.synthetic.main.activity_new_dairy.*

class NewDairyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_dairy)

        // TODO: ok 버튼

        // cancel 버튼
        tv_new_dairy_cancel.setOnClickListener{
            finish()
        }

        // 맵 뷰
        val tMapView = TMapView(this)
        tMapView.setSKTMapApiKey("l7xx575320139307413f9dd8196bf0803245")
        new_dairy_map_view.addView(tMapView)
    }
}
