package com.friendship41.cycledairy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.R
import kotlinx.android.synthetic.main.activity_list_dairy.*

class ListDairyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_dairy)

        // cancel 버튼
        tv_list_dairy_cancel.setOnClickListener{
            finish()
        }
    }
}
