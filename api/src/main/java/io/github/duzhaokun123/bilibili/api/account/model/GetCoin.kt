package io.github.duzhaokun123.bilibili.api.account.model


import com.google.gson.annotations.SerializedName

data class GetCoin(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("money")
        val money: Double
    )
}