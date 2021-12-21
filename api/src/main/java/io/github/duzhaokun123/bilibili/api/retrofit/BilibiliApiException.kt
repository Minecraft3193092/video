package io.github.duzhaokun123.bilibili.api.retrofit

import java.io.IOException

/**
 * 当服务器返回的 code 不等于 0 时抛出
 */
class BilibiliApiException(
    val commonResponse: CommonResponse
) : IOException(commonResponse.message?.takeIf { it.isNotEmpty() } ?: commonResponse.msg)