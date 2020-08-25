package com.friendship41.cycledairy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.activity.ListDairyActivity
import com.friendship41.cycledairy.activity.NewDairyActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // T-Map 연동
//        TMapView(this).setSKTMapApiKey("l7xx575320139307413f9dd8196bf0803245")

        // 리스트 보기
        tv_main_list.setOnClickListener {
            val listDairyIntent = Intent(this, ListDairyActivity::class.java)
            startActivity(listDairyIntent)
        }
        // 작성 페이지
        tv_main_new.setOnClickListener {
            val newDairyIntent = Intent(this, NewDairyActivity::class.java)
            startActivity(newDairyIntent)
        }
    }
}
