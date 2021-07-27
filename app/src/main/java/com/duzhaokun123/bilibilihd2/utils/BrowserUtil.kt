package com.duzhaokun123.bilibilihd2.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.CookieManager
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.duzhaokun123.bilibilihd2.R
import com.duzhaokun123.bilibilihd2.ui.UrlOpenActivity
import com.duzhaokun123.bilibilihd2.ui.WebViewActivity

object BrowserUtil {
    private const val TAG = "BrowserUtil"

    fun openInApp(
        context: Context?, url: String?
    ) {
        if (context == null || url == null) return
        try {
            context.startActivity(Intent(context, UrlOpenActivity::class.java).apply {
                data = Uri.parse(url)
            })
        } catch (e: Exception) {
            e.printStackTrace()
            TipUtil.showToast(e.message)
        }
    }

    fun openWebViewActivity(
        context: Context?, url: String?, desktopUA: Boolean = false,
        interceptAll: Boolean = false, finishWhenIntercept: Boolean = false
    ) {
        if (url != null)
            openWebViewActivity(context, url.toUri(), desktopUA, interceptAll, finishWhenIntercept)
    }

    fun openWebViewActivity(
        context: Context?, uri: Uri, desktopUA: Boolean = false,
        interceptAll: Boolean = false, finishWhenIntercept: Boolean = false
    ) {
        if (context == null) return
        val intent = Intent(context, WebViewActivity::class.java)
        intent.data = uri
        intent.putExtra(WebViewActivity.EXTRA_DESKTOP_UA, desktopUA)
        intent.putExtra(WebViewActivity.EXTRA_INTERCEPT_ALL, interceptAll)
        intent.putExtra(WebViewActivity.EXTRA_FINISH_WHEN_INTERCEPT, finishWhenIntercept)
        context.startActivity(intent)
    }

    fun openCustomTab(context: Context?, url: String) {
        Log.d(TAG, "openCustomTab: openUrl = $url")
        if (context == null) {
            Log.d(TAG, "openCustomTab: but context == null")
            return
        }
        try {
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(context.getColorCompat(R.color.primaryColor))
                        .build()
                )
                .build()
                .launchUrl(context, Uri.parse(url))
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    fun syncLoginResponseCookie() {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val loginResponse = bilibiliClient.loginResponse
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null)
        } else {
            cookieManager.removeAllCookie()
        }
        if (loginResponse == null) {
            return
        }
        for (url in loginResponse.data.cookieInfo.domains) {
            for ((_, _, name, value) in loginResponse.data.cookieInfo.cookies) {
                cookieManager.setCookie(url, "$name=$value")
            }
            cookieManager.setCookie(url, "Domain=$url")
            cookieManager.setCookie(url, "Path=/")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
        }
    }
}