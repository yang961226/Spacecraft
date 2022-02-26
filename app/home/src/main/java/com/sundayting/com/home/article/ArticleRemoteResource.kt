package com.sundayting.com.home.article

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class ArticleRemoteResource @Inject constructor(
    private val wanAppService: WanAppService,
) {

    suspend fun getArticle(page: Int) = wanAppService.article(page)

    suspend fun collectArticle(id: Int) = wanAppService.collect(id)

}