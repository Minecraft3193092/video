package com.hiczp.bilibili.api.retrofit.exception

import com.hiczp.bilibili.api.retrofit.CommonResponse
import java.io.IOException

/**
 * 当服务器返回的 code 不等于 0 时抛出
 */
class BilibiliApiException(
        val commonResponse: CommonResponse
) : IOException(commonResponse.message?.takeIf { it.isNotEmpty() } ?: commonResponse.msg)