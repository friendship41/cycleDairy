package com.friendship41.cycledairy.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class DairyRecord constructor(var dairyRecordSeq: Int? = null,
                              var memberId: String? = null,
                              var startLocationName: String? = null,
                              var startLocationLat: Double? = null,
                              var startLocationLon: Double? = null,
                              var endLocationName: String? = null,
                              var endLocationLat: Double? = null,
                              var endLocationLon: Double? = null,
                              var dairyDate: Date? = null) : Parcelable

data class ResponseDairyRecordList constructor(var responseCode: String, var responseMessage: String, var data: List<DairyRecord>?)
