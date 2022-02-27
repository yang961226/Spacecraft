package com.sundayting.com.home.article

import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleRemoteResource: ArticleRemoteResource,
) {

    suspend fun getArticle(page: Int) = articleRemoteResource.getArticle(page)

    suspend fun collectArticle(id: Long) = articleRemoteResource.collectArticle(id)

    suspend fun unCollectArticle(id: Long) = articleRemoteResource.unCollectArticle(id)

}