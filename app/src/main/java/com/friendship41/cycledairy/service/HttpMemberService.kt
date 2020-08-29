package com.friendship41.cycledairy.service

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.friendship41.cycledairy.PreActivity
import com.friendship41.cycledairy.activity.MemberLoginActivity
import com.friendship41.cycledairy.common.CYCLE_DAIRY_SERVER
import com.friendship41.cycledairy.common.URL_GET_CHECK_TOKEN
import com.friendship41.cycledairy.common.URL_POST_LOGIN
import com.friendship41.cycledairy.data.CycleMember
import com.friendship41.cycledairy.data.ResponseToken
import com.google.gson.Gson

object HttpMemberService {
    fun login(context: AppCompatActivity, id: String, password: String) {
        val stringRequest = object : StringRequest(
            Method.POST,
            CYCLE_DAIRY_SERVER + URL_POST_LOGIN,
            Response.Listener { response ->
                println(response)
                val responseToken = Gson().fromJson<ResponseToken>(response, ResponseToken::class.java)
                when (responseToken.result) {
                    "200" -> {
                        PreActivity.prefs.setString("access_token", responseToken.access_token ?: "")
                        PreActivity.prefs.setString("memberId", id)
                        context.finish()
                    }
                    else -> {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("로그인 실패!")
                        builder.setMessage("아이디/비번을 확인해 주세요")
                        builder.show()
                    }
                }
            }, Response.ErrorListener { error ->
                println("error: $error")
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getBody(): ByteArray {
                return Gson().toJson(CycleMember(id, password, null)).toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun checkToken(context: AppCompatActivity, accessToken: String) {
        val stringRequest = object : StringRequest(
            Method.GET,
            CYCLE_DAIRY_SERVER + URL_GET_CHECK_TOKEN,
            Response.Listener { response ->
                run {
                    println("response: $response")
                    val responseToken = Gson().fromJson<ResponseToken>(response, ResponseToken::class.java)
                    when (responseToken.result) {
                        "200" -> {}
                        else -> context.startActivity(Intent(context, MemberLoginActivity::class.java))
                    }
                }
            }, Response.ErrorListener { error ->
                println("error: $error")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headerMap = HashMap<String, String>()
                headerMap.put("access_token", accessToken)
                return headerMap
            }
        }
        Volley.newRequestQueue(context).add(stringRequest)
    }
}
