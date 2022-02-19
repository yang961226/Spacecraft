package com.sundayting.com.network.okhttp

import com.sundayting.com.network.ext.containMethodAnnotation
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Streaming

class HttpLoggingInterceptorProMaxX(
    private val httpLoggingInterceptor: HttpLoggingInterceptor
) : Interceptor {

    companion object {
        private val basicHttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //包含[Streaming]注解，说明是文件下载
        if (request.containMethodAnnotation(Streaming::class.java)) {
            return basicHttpLoggingInterceptor.intercept(chain)
        }
        return httpLoggingInterceptor.intercept(chain)
    }

}