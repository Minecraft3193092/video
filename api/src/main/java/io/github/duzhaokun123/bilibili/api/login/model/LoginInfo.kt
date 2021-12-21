package io.github.duzhaokun123.bilibili.api.login.model

import com.google.gson.annotations.SerializedName

data class LoginInfo(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("ts")
    val ts: Long,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val `data`: Any
)
