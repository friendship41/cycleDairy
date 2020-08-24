package com.friendship41.cycledairy.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R

class ListDairyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_dairy)

        // cancel 버튼
        findViewById<TextView>(R.id.tv_list_dairy_cancel).setOnClickListener {
            finish()
        }
    }
}
