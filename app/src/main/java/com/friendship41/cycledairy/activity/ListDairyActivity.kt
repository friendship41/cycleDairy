package com.friendship41.cycledairy.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.friendship41.cycledairy.PreActivity
import com.friendship41.cycledairy.R
import com.friendship41.cycledairy.checkLogin
import com.friendship41.cycledairy.service.HttpDairyRecordService
import com.friendship41.cycledairy.data.DairyRecord
import kotlinx.android.synthetic.main.activity_list_dairy.*

class ListDairyActivity : AppCompatActivity() {
    var dairyRecordList: List<DairyRecord>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_dairy)

        // TODO: 로그인 기능 완료 후 아이디 대입
        HttpDairyRecordService.getRecordList(this, "test", PreActivity.prefs.getString("access_token", ""))

        // 리스트뷰 클릭
        lv_list_dairy.setOnItemClickListener { _, _, position, _ ->
            val intentToShowRecord = Intent(this, ShowDairyRecordActivity::class.java)
            intentToShowRecord.putExtra("dairyRecord", dairyRecordList?.get(position))
            this.startActivity(intentToShowRecord)
        }

        // cancel 버튼
        tv_list_dairy_cancel.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        checkLogin(this, PreActivity.prefs.getString("access_token", ""))
    }
}
