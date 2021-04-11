package com.hiczp.bilibili.api.retrofit.interceptor

import com.hiczp.bilibili.api.retrofit.*
import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

private val logger = KotlinLogging.logger {}

/**
 * 为请求添加公共参数
 * 如果请求为 GET 方式则添加到 Query, 如果是其他其他方式则尝试添加到 BODY.
 *
 * @param additionParams ParamName to ParamValueExpression
 */
class CommonParamInterceptor(private vararg val additionParams: ParamExpression) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var headers = request.headers
        var httpUrl = request.url
        var body = request.body

        //是否强制加到 Query(暂不存在强制加到 FormBody 的情况)
        var forceQuery = false
        val forceParam = headers[Header.FORCE_PARAM]
        if (forceParam != null) {
            if (forceParam == Header.FORCE_PARAM_QUERY) forceQuery = true
            headers = headers.newBuilder().removeAll(Header.FORCE_PARAM).build()
        }

        when {
            //如果是 GET 则添加到 Query
            request.method == Method.GET || forceQuery -> {
                httpUrl = request.url.newBuilder().apply {
                    additionParams.forEachNonNull { name, value ->
                        addQueryParameter(name, value)
                    }
                }.build()
            }

            //如果 Body 不存在或者为空则创建一个 FormBody
            body == null || body.contentLength() == 0L -> {
                body = FormBody.Builder().apply {
                    additionParams.forEachNonNull { name, value ->
                        add(name, value)
                    }
                }.build()
            }

            //如果 Body 为 FormBody 则里面可能已经存在内容
            body is FormBody -> {
                body = FormBody.Builder().addAllEncoded(body).apply {
                    additionParams.forEachNonNull { name, value ->
                        add(name, value)
                    }
                }.build()
            }

            //如果方式不为 GET 且 Body 不为空或者为 FormBody 则无法添加公共参数
            else -> {
                logger.error {
                    "Cannot add params to request: ${request.method} ${request.url} ${body.javaClass.simpleName}"
                }
            }
        }

        return chain.proceed(
                request.newBuilder()
                        .headers(headers)
                        .url(httpUrl)
                        .method(request.method, body)
                        .build()
        )
    }
}
