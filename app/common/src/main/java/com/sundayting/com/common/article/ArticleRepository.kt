package com.sundayting.com.common.article

import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleRemoteResource: ArticleRemoteResource,
) {

    suspend fun getArticle(page: Int) = articleRemoteResource.getArticle(page)

    suspend fun collectArticle(id: Long) = articleRemoteResource.collectArticle(id)

    suspend fun unCollectArticle(id: Long) = articleRemoteResource.unCollectArticle(id)

    suspend fun getArticleCollected(page: Int) = articleRemoteResource.getArticleCollected(page)

}