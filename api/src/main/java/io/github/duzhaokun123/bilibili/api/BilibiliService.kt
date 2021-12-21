package io.github.duzhaokun123.bilibili.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.github.duzhaokun123.bilibili.api.account.AccountApi
import io.github.duzhaokun123.bilibili.api.api.ApiApi
import io.github.duzhaokun123.bilibili.api.login.LoginApi
import io.github.duzhaokun123.bilibili.api.retrofit.BaseUrl
import io.github.duzhaokun123.bilibili.api.retrofit.CommonCookieInterceptor
import io.github.duzhaokun123.bilibili.api.retrofit.FailureResponseInterceptor
import io.github.duzhaokun123.bilibili.api.user.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object BilibiliService {
    private val gsonConverterFactory by lazy { GsonConverterFactory.create() }
    private val coroutineCallAdapterFactory by lazy { CoroutineCallAdapterFactory() }
    private val httpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

    private val cookieInterceptor by lazy {
        CommonCookieInterceptor(
            Cookies.DEDE_USER_ID to { cookies?.dedeUserID },
            Cookies.DEDE_USER_ID_CKMD5 to { cookies?.dedeUserIDCkMd5 },
            Cookies.SESSDATA to { cookies?.sessdata },
            Cookies.BILI_JCT to { cookies?.biliJct },
            Cookies.SID to { cookies?.sid },
        )
    }

    var cookies: Cookies? = null

    val loggedIn get() = cookies != null

    private inline fun <reified T> genApi() =
        Retrofit.Builder()
            .baseUrl(T::class.java.getAnnotation(BaseUrl::class.java).url)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .client(OkHttpClient.Builder().apply {
                addInterceptor(FailureResponseInterceptor)
                addInterceptor(cookieInterceptor)
                addNetworkInterceptor(httpLoggingInterceptor)
            }.build())
            .build()
            .create<T>()

    fun setLogLevel(logLevel: HttpLoggingInterceptor.Level) {
        httpLoggingInterceptor.level = logLevel
    }

    val userApi = genApi<UserApi>()
    val loginApi = genApi<LoginApi>()
    val accountApi = genApi<AccountApi>()
    val apiApi = genApi<ApiApi>()
}