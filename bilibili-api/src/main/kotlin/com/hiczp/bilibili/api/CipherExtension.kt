package com.hiczp.bilibili.api

import java.security.MessageDigest

//MD5
private val md5Instance = MessageDigest.getInstance("MD5")

fun String.md5() =
        StringBuilder(32).apply {
            //优化过的 md5 字符串生成算法
            md5Instance.digest(toByteArray()).forEach {
                val value = it.toInt() and 0xFF
                val high = value / 16
                val low = value % 16
                append(if (high <= 9) '0' + high else 'a' - 10 + high)
                append(if (low <= 9) '0' + low else 'a' - 10 + low)
            }
        }.toString()

/**
 * 签名算法为 "$排序后的参数字符串$appSecret".md5()
 */
internal fun calculateSign(sortedQuery: String, appSecret: String) = (sortedQuery + appSecret).md5()
