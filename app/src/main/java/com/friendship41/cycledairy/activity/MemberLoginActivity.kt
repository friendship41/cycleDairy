package com.friendship41.cycledairy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.friendship41.cycledairy.R
import com.friendship41.cycledairy.service.HttpMemberService
import kotlinx.android.synthetic.main.activity_member_login.*

class MemberLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_login)

        btn_member_login_login.setOnClickListener {
            HttpMemberService.login(
                this,
                edt_member_login_id.text.toString(),
                edt_member_login_password.text.toString()
            )
        }
    }
}
