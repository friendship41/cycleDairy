package com.friendship41.cycledairy.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R
import kotlinx.android.synthetic.main.activity_new_dairy.*
import net.daum.mf.map.api.MapView

class NewDairyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_dairy)

        // TODO: ok 버튼

        // cancel 버튼
        findViewById<TextView>(R.id.tv_new_dairy_cancel).setOnClickListener {
            finish()
        }

        // 맵 뷰
        val mapView = MapView(this)
        mapView.setDaumMapApiKey("550ee1ab2a7332def7d3a9f9a5296e18")
        val mapViewContainer = map_view
        mapViewContainer.addView(mapView)

    }
}
