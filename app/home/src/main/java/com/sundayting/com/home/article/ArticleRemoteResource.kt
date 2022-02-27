package com.sundayting.com.home.article

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class ArticleRemoteResource @Inject constructor(
    private val wanAppService: WanAppService,
) {

    suspend fun getArticle(page: Int) = wanAppService.article(page)

    suspend fun collectArticle(id: Long) = wanAppService.collect(id)

    suspend fun unCollectArticle(id: Long) = wanAppService.unCollect(id)

}