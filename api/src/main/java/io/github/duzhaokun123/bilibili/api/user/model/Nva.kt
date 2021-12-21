package io.github.duzhaokun123.bilibili.api.user.model


import com.google.gson.annotations.SerializedName

data class Nva(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("ttl")
    val ttl: Int
) {
    data class Data(
        @SerializedName("allowance_count")
        val allowanceCount: Int,
        @SerializedName("answer_status")
        val answerStatus: Int,
        @SerializedName("email_verified")
        val emailVerified: Int,
        @SerializedName("face")
        val face: String,
        @SerializedName("has_shop")
        val hasShop: Boolean,
        @SerializedName("isLogin")
        val isLogin: Boolean,
        @SerializedName("level_info")
        val levelInfo: LevelInfo,
        @SerializedName("mid")
        val mid: Int,
        @SerializedName("mobile_verified")
        val mobileVerified: Int,
        @SerializedName("money")
        val money: Double,
        @SerializedName("moral")
        val moral: Int,
        @SerializedName("official")
        val official: Official,
        @SerializedName("officialVerify")
        val officialVerify: OfficialVerify,
        @SerializedName("pendant")
        val pendant: Pendant,
        @SerializedName("scores")
        val scores: Int,
        @SerializedName("shop_url")
        val shopUrl: String,
        @SerializedName("uname")
        val uname: String,
        @SerializedName("vip_avatar_subscript")
        val vipAvatarSubscript: Int,
        @SerializedName("vipDueDate")
        val vipDueDate: Long,
        @SerializedName("vip_label")
        val vipLabel: VipLabel,
        @SerializedName("vip_nickname_color")
        val vipNicknameColor: String,
        @SerializedName("vip_pay_type")
        val vipPayType: Int,
        @SerializedName("vipStatus")
        val vipStatus: Int,
        @SerializedName("vip_theme_type")
        val vipThemeType: Int,
        @SerializedName("vipType")
        val vipType: Int,
        @SerializedName("wallet")
        val wallet: Wallet
    ) {
        data class LevelInfo(
            @SerializedName("current_exp")
            val currentExp: Int,
            @SerializedName("current_level")
            val currentLevel: Int,
            @SerializedName("current_min")
            val currentMin: Int,
            @SerializedName("next_exp")
            val nextExp: Int
        )

        data class Official(
            @SerializedName("desc")
            val desc: String,
            @SerializedName("role")
            val role: Int,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int
        )

        data class OfficialVerify(
            @SerializedName("desc")
            val desc: String,
            @SerializedName("type")
            val type: Int
        )

        data class Pendant(
            @SerializedName("expire")
            val expire: Int,
            @SerializedName("image")
            val image: String,
            @SerializedName("image_enhance")
            val imageEnhance: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("pid")
            val pid: Int
        )

        data class VipLabel(
            @SerializedName("label_theme")
            val labelTheme: String,
            @SerializedName("path")
            val path: String,
            @SerializedName("text")
            val text: String
        )

        data class Wallet(
            @SerializedName("bcoin_balance")
            val bcoinBalance: Int,
            @SerializedName("coupon_balance")
            val couponBalance: Int,
            @SerializedName("coupon_due_time")
            val couponDueTime: Int,
            @SerializedName("mid")
            val mid: Int
        )
    }
}