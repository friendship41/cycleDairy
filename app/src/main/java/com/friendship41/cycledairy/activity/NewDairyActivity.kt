package com.friendship41.cycledairy.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R

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

    }
}
