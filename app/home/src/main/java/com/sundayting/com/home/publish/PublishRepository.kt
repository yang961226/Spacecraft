package com.sundayting.com.home.publish

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class PublishRepository @Inject constructor(
    private val wanAppService: WanAppService,
) {

    suspend fun publish(title: String, link: String) = wanAppService.publishArticle(title, link)

}