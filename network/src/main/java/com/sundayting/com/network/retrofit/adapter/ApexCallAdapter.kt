package com.sundayting.com.network.retrofit.adapter

import com.sundayting.com.network.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApexCallAdapter constructor(
    private val resultType: Type
) : CallAdapter<Type, Call<ApiResponse<Type>>> {
    override fun responseType() = resultType

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> =
        ApexResponseCallDelegate(call)

}