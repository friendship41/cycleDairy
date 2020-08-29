package com.friendship41.cycledairy.service

import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.friendship41.cycledairy.R
import com.friendship41.cycledairy.activity.ListDairyActivity
import com.friendship41.cycledairy.activity.ParcelablePOI
import com.friendship41.cycledairy.common.CYCLE_DAIRY_SERVER
import com.friendship41.cycledairy.common.RESULT_CODE_SUCCESS
import com.friendship41.cycledairy.common.URL_GET_DAIRY_RECORD
import com.friendship41.cycledairy.common.URL_POST_DAIRY_RECORD
import com.friendship41.cycledairy.data.DairyRecord
import com.friendship41.cycledairy.data.ResponseDairyRecordList
import com.google.gson.Gson

object HttpDairyRecordService {
    fun postRecord(context: AppCompatActivity, dairyRecord: DairyRecord, accessToken: String) {
        val stringRequest = object : StringRequest(
            Method.POST,
            CYCLE_DAIRY_SERVER + URL_POST_DAIRY_RECORD,
            Response.Listener { response ->
                run {
                    println("response: $response")
                    val dairyRecordResponse = Gson().fromJson<DairyRecord>(response.toString(), DairyRecord::class.java)
                    val intent = Intent()
                    intent.putExtra("poiFrom", ParcelablePOI(
                        "${dairyRecordResponse.dairyRecordSeq}",
                        dairyRecordResponse.startLocationName ?: "",
                        "${dairyRecordResponse.startLocationLat}",
                        "${dairyRecordResponse.startLocationLon}"))
                    intent.putExtra("poiTo", ParcelablePOI(
                        "${dairyRecordResponse.dairyRecordSeq}",
                        dairyRecordResponse.endLocationName ?: "",
                        "${dairyRecordResponse.endLocationLat}",
                        "${dairyRecordResponse.endLocationLon}"
                    ))
                    context.setResult(RESULT_CODE_SUCCESS, intent)
                    context.finish()
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
            override fun getBodyContentType(): String {
                return "application/json"
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return Gson().toJson(dairyRecord).toString().toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(stringRequest)
    }

    fun getRecordList(context: ListDairyActivity, memberId: String, accessToken: String) {
        val stringRequest = object : StringRequest(
            Method.GET,
            "$CYCLE_DAIRY_SERVER$URL_GET_DAIRY_RECORD?returnType=list&memberId=$memberId",
            Response.Listener { response ->
                run {
                    println("response: $response")
                    val responseDairyRecordList = Gson().fromJson<ResponseDairyRecordList>(response.toString(), ResponseDairyRecordList::class.java)
                    context.dairyRecordList = responseDairyRecordList.data
                    // TODO: 커스텀 리스트로 이쁘게 보이게...
                    context.findViewById<ListView>(R.id.lv_list_dairy).adapter = responseDairyRecordList.data?.let {
                        ArrayAdapter<DairyRecord>(
                            context,
                            android.R.layout.simple_list_item_1,
                            it
                        )
                    }
                }
            }, Response.ErrorListener { error ->
                println("error: $error")
                context.finish()
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
