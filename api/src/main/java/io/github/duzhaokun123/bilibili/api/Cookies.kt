package io.github.duzhaokun123.bilibili.api

import retrofit2.Response

data class Cookies(
    val dedeUserID: String,
    val dedeUserIDCkMd5: String,
    val sessdata: String,
    val biliJct: String,
    val sid: String,
    val expires: String
) {
    companion object {
        const val DEDE_USER_ID = "DedeUserID"
        const val DEDE_USER_ID_CKMD5 = "DedeUserID__ckMd5"
        const val SESSDATA = "SESSDATA"
        const val BILI_JCT = "bili_jct"
        const val SID = "sid"

        fun fromResponse(response: Response<*>): Cookies {
            val setCookies = response.headers().values("Set-Cookie")
            return Cookies(
                setCookies.getCookie(DEDE_USER_ID),
                setCookies.getCookie(DEDE_USER_ID_CKMD5),
                setCookies.getCookie(SESSDATA),
                setCookies.getCookie(BILI_JCT),
                setCookies.getCookie(SID),
                response.headers().values("Expires")[0]
            )
        }

        private fun List<String>.getCookie(name: String) = this.first { it.startsWith(name) }.run { substring(indexOf("=") + 1, indexOf(";")) }
    }

    val uid get() = dedeUserID.toLong()
}
