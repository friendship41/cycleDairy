package com.friendship41.cycledairy.data

import java.util.*


data class DairyRecord constructor(var dairyRecordSeq: Int? = null,
                              var memberId: String? = null,
                              var startLocationName: String? = null,
                              var startLocationLat: Double? = null,
                              var startLocationLon: Double? = null,
                              var endLocationName: String? = null,
                              var endLocationLat: Double? = null,
                              var endLocationLon: Double? = null,
                              var dairyDate: Date? = null)

data class ResponseDairyRecord constructor(var responseCode: String, var responseMessage: String, var data: DairyRecord?)

data class ResponseDairyRecordList constructor(var responseCode: String, var responseMessage: String, var data: List<DairyRecord>?)
