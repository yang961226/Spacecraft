package com.sundayting.com.common

import com.sundayting.com.common.bean.ArticleListBean
import com.sundayting.com.common.bean.BannerBean
import com.sundayting.com.common.bean.UserBean
import com.sundayting.com.common.bean.WanBeanWrapper
import com.sundayting.com.network.ApiResponse
import retrofit2.http.*

interface WanAppService {

    /**
     * 注册
     */
    @POST("/user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") rePassword: String
    ): ApiResponse<WanBeanWrapper<Any>>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse<WanBeanWrapper<UserBean>>

    /**
     * 首页Banner
     */
    @GET("/banner/json")
    suspend fun banner(
        //无参数
    ): ApiResponse<WanBeanWrapper<MutableList<BannerBean>>>

    /**
     * 获取首页文章数据
     */
    @GET("/article/list/{page}/json")
    suspend fun article(
        @Path("page") page: Int
    ): ApiResponse<WanBeanWrapper<ArticleListBean>>

    /**
     * 收藏文章
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Long): ApiResponse<WanBeanWrapper<Any>>

    /**
     * 取消收藏文章
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Long): ApiResponse<WanBeanWrapper<Any>>

}