package com.sundayting.com.network.retrofit.adapter

import com.sundayting.com.network.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApexCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): ApexCallAdapter? = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                ApiResponse::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ApexCallAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }

    companion object {

        fun create(): ApexCallAdapterFactory = ApexCallAdapterFactory()

    }

}