package io.github.duzhaokun123.bilibili.api.account

import io.github.duzhaokun123.bilibili.api.account.model.GetCoin
import io.github.duzhaokun123.bilibili.api.retrofit.BaseUrl
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

@BaseUrl("https://account.bilibili.com")
interface AccountApi {
    @GET("/site/getCoin")
    fun getCoin(): Deferred<GetCoin>
}