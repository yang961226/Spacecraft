package com.sundayting.com.common

import com.sundayting.com.common.bean.BeanWrapper
import com.sundayting.com.common.bean.UserBean
import com.sundayting.com.network.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface WanAppService {

    /**
     * 注册
     */
    @POST("/user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") rePassword: String
    ): ApiResponse<BeanWrapper<Unit>>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<BeanWrapper<UserBean>>

}