package com.hiczp.bilibili.api.main.model

import com.google.gson.annotations.SerializedName

data class CreateFavoriteFolderResponse(
        @SerializedName("code")
        var code: Int, // 0
        @SerializedName("data")
        var `data`: Data,
        @SerializedName("message")
        var message: String, // 0
        @SerializedName("ttl")
        var ttl: Int // 1
) {
    data class Data(
            @SerializedName("fid")
            var fid: Long // 3326376
    )
}
