package io.github.duzhaokun123.bilibili.api.login.model


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("ts")
    val ts: Long?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val `data`: Data
) {
    data class Data(
        // 未登录时
        @SerializedName("redirectUrl")
        val redirectUrl: String?,

        // 已登录时
        @SerializedName("isLogin")
        val isLogin: Boolean?,
        @SerializedName("goUrl")
        val goUrl: String?,

        // 需验证手机号或邮箱时
        @SerializedName("mid")
        val mid: Long?,
        @SerializedName("tel")
        val tel: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("sorce")
        val sorce: Int?,
        @SerializedName("keeptime")
        val keeptime: Int?,
        // goUrl
    )
}