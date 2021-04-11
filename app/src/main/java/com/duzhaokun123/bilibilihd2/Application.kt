package com.duzhaokun123.bilibilihd2

import androidx.appcompat.app.AppCompatDelegate
import com.duzhaokun123.bilibilihd2.utils.BrowserUtil
import com.duzhaokun123.bilibilihd2.utils.UsersMap
import com.duzhaokun123.generated.Settings
import com.hiczp.bilibili.api.BilibiliClient
import com.hiczp.bilibili.api.BilibiliClientProperties

@Suppress("UNUSED")
class Application : android.app.Application() {
    companion object {
        lateinit var instance: Application
            private set
        lateinit var bilibiliClient: BilibiliClient
            private set
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        //init
        Settings.init(this)
        UsersMap.reload()
        reinitBilibiliClient()
        reinitUiMod()
    }

    fun reinitBilibiliClient(
        uid: Long = Settings.selectedUid,
        userAppKey: String = Settings.appKey!!,
        userAppSec: String = Settings.appSec!!
    ) {
        bilibiliClient = BilibiliClient(object : BilibiliClientProperties {
            override val appKey: String
                get() = if (userAppKey.isEmpty()) super.appKey else userAppKey
            override val appSecret: String
                get() = if (userAppSec.isEmpty()) super.appSecret else userAppSec
        })
        bilibiliClient.loginResponse = UsersMap[uid]
        BrowserUtil.syncLoginResponseCookie()
    }

    fun reinitUiMod(uiMod: Int = Settings.uiMod) {
        AppCompatDelegate.setDefaultNightMode(
            when (uiMod) {
                1 -> AppCompatDelegate.MODE_NIGHT_NO
                2 -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }
}