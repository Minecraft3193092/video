package io.github.duzhaokun123.bilibili.api.user

import io.github.duzhaokun123.bilibili.api.retrofit.BaseUrl
import io.github.duzhaokun123.bilibili.api.user.model.Nva
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

@BaseUrl("https://api.bilibili.com")
interface UserApi {
    @GET("/nav")
    fun nav(): Deferred<Nva>
}