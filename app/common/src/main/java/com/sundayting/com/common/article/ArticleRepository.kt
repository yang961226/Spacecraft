package com.sundayting.com.common.article

import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleRemoteSource: ArticleRemoteSource,
) {

    fun getArticlePagingData() = articleRemoteSource.getArticlePagingData()

    suspend fun getArticle(page: Int) = articleRemoteSource.getArticle(page)

    suspend fun collectArticle(id: Long) = articleRemoteSource.collectArticle(id)

    suspend fun unCollectArticle(id: Long) = articleRemoteSource.unCollectArticle(id)

    suspend fun getArticleCollected(page: Int) = articleRemoteSource.getArticleCollected(page)

}