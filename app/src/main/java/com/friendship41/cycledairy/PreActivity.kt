package com.friendship41.cycledairy

import android.app.Application
import com.friendship41.cycledairy.common.SharedPreferenceUtil

class PreActivity: Application() {
    companion object {
        lateinit var prefs: SharedPreferenceUtil
    }

    override fun onCreate() {
        prefs = SharedPreferenceUtil(applicationContext)
        super.onCreate()
    }
}
