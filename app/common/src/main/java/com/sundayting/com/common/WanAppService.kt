package com.sundayting.com.common

import com.sundayting.com.common.bean.*
import com.sundayting.com.network.ApiResponse
import retrofit2.http.*

interface WanAppService {

    /**
     * 注册
     */
    @FormUrlEncoded
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

    @GET("/user/logout/json")
    suspend fun logout(): ApiResponse<WanBeanWrapper<Any>>

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
     * 获取收藏的文章
     * @param page Int 页数
     */
    @GET("/lg/collect/list/{page}/json")
    suspend fun getArticleCollected(@Path("page") page: Int): ApiResponse<WanBeanWrapper<ArticleListBean>>

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

    /**
     * 发表文章
     */
    @POST("/lg/user_article/add/json")
    suspend fun publishArticle(@Query("title") title: String, @Query("link") link: String)
            : ApiResponse<WanBeanWrapper<Any>>

    /**
     * 积分记录
     */
    @GET("/lg/coin/list/{pageNum}/json")
    suspend fun getIntegralRecord(@Path("pageNum") page: Long): ApiResponse<WanBeanWrapper<ListBean<IntegralBean>>>

}