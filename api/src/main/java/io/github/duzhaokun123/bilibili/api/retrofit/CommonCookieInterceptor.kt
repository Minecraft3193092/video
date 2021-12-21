package io.github.duzhaokun123.bilibili.api.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class CommonCookieInterceptor(private vararg val additionCookies: Pair<String, () -> String?>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            additionCookies.forEach { (name, value) ->
                val v = value() ?: return@forEach
                addHeader("Cookie", "$name=$v")
            }
        }.build()
        return chain.proceed(request)
    }
}