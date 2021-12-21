package io.github.duzhaokun123.bilibili.api.api

import io.github.duzhaokun123.bilibili.api.api.model.History
import io.github.duzhaokun123.bilibili.api.retrofit.BaseUrl
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

@BaseUrl("https://api.bilibili.com")
interface ApiApi {
    @GET("x/web-interface/history/cursor")
    fun getHistory(
        @Query("max") max: Int? = null,
        @Query("business") business: String? = null,
        @Query("view_at") viewAt: Long? = 0,
        @Query("ps") ps: Int? = 20
    ): Deferred<History>
}