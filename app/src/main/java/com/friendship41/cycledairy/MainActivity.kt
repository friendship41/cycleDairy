package com.friendship41.cycledairy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.activity.ListDairyActivity
import com.friendship41.cycledairy.activity.NewDairyActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 리스트 보기
        findViewById<TextView>(R.id.tv_main_list).setOnClickListener {
            val listDairyIntent = Intent(this, ListDairyActivity::class.java)
            startActivity(listDairyIntent)
        }
        // 작성 페이지
        findViewById<TextView>(R.id.tv_main_new).setOnClickListener {
            val newDairyIntent = Intent(this, NewDairyActivity::class.java)
            startActivity(newDairyIntent)
        }
    }
}
