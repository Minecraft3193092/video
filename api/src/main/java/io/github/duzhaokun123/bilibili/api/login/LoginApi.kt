package io.github.duzhaokun123.bilibili.api.login

import io.github.duzhaokun123.bilibili.api.login.model.LoginInfo
import io.github.duzhaokun123.bilibili.api.login.model.LoginUrl
import io.github.duzhaokun123.bilibili.api.login.model.LoginResponse
import io.github.duzhaokun123.bilibili.api.retrofit.BaseUrl
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

@BaseUrl("https://passport.bilibili.com")
interface LoginApi {
    @FormUrlEncoded
    @POST("/web/login/v2")
    fun webLogin(
        @Field("captchaType") captchaType: Int = 6,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("keep") keep: Boolean = true,
        @Field("key") key: String,
        @Field("challenge") challenge: String,
        @Field("validate") validate: String,
        @Field("seccode") seccode: String,
    ): Deferred<LoginResponse>

    @GET("qrcode/getLoginUrl")
    fun getLoginUrl(): Deferred<LoginUrl>

    @FormUrlEncoded
    @POST("qrcode/getLoginInfo")
    fun getLoginInfo(
        @Field("oauthKey") oauthKey: String,
        @Field("gourl") goUrl: String? = "https://www.bilibili.com"
    ): Deferred<Response<LoginInfo>>
}