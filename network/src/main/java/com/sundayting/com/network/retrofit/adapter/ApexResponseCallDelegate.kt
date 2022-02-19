package com.sundayting.com.network.retrofit.adapter

import com.sundayting.com.network.ApiResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class ApexResponseCallDelegate<T>(private val proxyCall: Call<T>) :
    Call<ApiResponse<T>> {

    override fun enqueue(callback: Callback<ApiResponse<T>>) =
        proxyCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@ApexResponseCallDelegate,
                    Response.success(
                        ApiResponse.of { response }
                    )
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ApexResponseCallDelegate,
                    Response.success(ApiResponse.exception(t))
                )
            }

        })

    override fun isExecuted(): Boolean = proxyCall.isExecuted

    override fun cancel() = proxyCall.cancel()

    override fun isCanceled(): Boolean = proxyCall.isCanceled

    override fun request(): Request = proxyCall.request()

    override fun timeout(): Timeout = proxyCall.timeout()

    override fun clone(): Call<ApiResponse<T>> =
        ApexResponseCallDelegate(proxyCall.clone())

    override fun execute(): Response<ApiResponse<T>> = throw NotImplementedError()


}