package com.sundayting.com.mine.myarticle

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class MyArticleRepository @Inject constructor(
    private val wanAppService: WanAppService,
) {

    suspend fun getMySharedArticle(page: Long) = wanAppService.getMySharedArticle(page)

}