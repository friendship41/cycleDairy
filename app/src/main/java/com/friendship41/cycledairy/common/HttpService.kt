package com.friendship41.cycledairy.common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.friendship41.cycledairy.activity.ParcelablePOI
import com.friendship41.cycledairy.data.DairyRecord
import com.google.gson.Gson

object HttpService {
    fun postRecord(context: AppCompatActivity, dairyRecord: DairyRecord) {
        val stringRequest = object : StringRequest(
            Method.POST,
            CYCLE_DAIRY_SERVER+URL_POST_DAIRY_RECORD,
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
            }) {
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
}
