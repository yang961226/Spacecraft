package com.sundayting.com.common.article

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class ArticleRemoteSource @Inject constructor(
    private val articlePagingSource: ArticlePagingSource,
    private val wanAppService: WanAppService,
) {

    fun getArticlePagingData() = Pager(
        config = PagingConfig(
            ArticlePagingSource.DEFAULT_PAGE_SIZE
        ),
        pagingSourceFactory = {
            articlePagingSource
        }
    ).flow

    @Deprecated("废弃，使用getArticlePagingData()")
    suspend fun getArticle(page: Int) = wanAppService.article(page)

    suspend fun collectArticle(id: Long) = wanAppService.collect(id)

    suspend fun unCollectArticle(id: Long) = wanAppService.unCollect(id)

    suspend fun getArticleCollected(page: Int) = wanAppService.getArticleCollected(page)

}