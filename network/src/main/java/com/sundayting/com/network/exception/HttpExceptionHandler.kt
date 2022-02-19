package com.mastercom.mtcorex.network.exception

import com.google.gson.JsonParseException
import com.squareup.moshi.JsonDataException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

/**
 * 网络异常统一处理工具
 */
class HttpExceptionHandler {

    interface Factory {
        fun parseExceptionMessage(throwable: Throwable?): String
    }

    /**
     * 解析器的默认实现
     */
    companion object : Factory {
        override fun parseExceptionMessage(throwable: Throwable?): String {
            return when (throwable) {
                // TODO: 2021/11/8 此列表不一定全面，而且以后会改成可配置性，仅供参考
                is ConnectException, is SocketException, is HttpException, is UnknownHostException -> "网络连接失败"
                is SSLHandshakeException -> "证书验证失败"
                is JSONException, is ParseException, is JsonParseException, is JsonDataException -> "解析报文失败"
                is SocketTimeoutException -> "连接超时"
                is MsgException -> throwable.tip
                else -> "未知错误"
            }
        }

    }

}