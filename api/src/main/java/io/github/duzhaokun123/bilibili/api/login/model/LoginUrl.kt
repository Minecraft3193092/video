package io.github.duzhaokun123.bilibili.api.login.model


import com.google.gson.annotations.SerializedName

data class LoginUrl(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("ts")
    val ts: Int
) {
    data class Data(
        @SerializedName("oauthKey")
        val oauthKey: String,
        @SerializedName("url")
        val url: String
    )
}