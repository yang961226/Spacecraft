package com.sundayting.com.common.article

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sundayting.com.common.WanAppService
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.network.onException
import com.sundayting.com.network.onSuccess
import java.io.IOException
import javax.inject.Inject

class ArticlePagingSource @Inject constructor(
    private val wanAppService: WanAppService,
) : PagingSource<Int, ArticleBean>() {

    companion object {
        //默认的分页数量
        const val DEFAULT_PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleBean> {
        val page = params.key ?: 0
        wanAppService.article(page)
            .onSuccess {
                if (it.responseBody.isSuccessful()) {
                    val articles = it.responseBody.data
                    if (articles == null) {
                        return LoadResult.Error(IOException("获取文章失败"))
                    } else {
                        val prevKey = if (page > 0) page - 1 else null
                        val nextKey = if (articles.datas.isNotEmpty()) page + 1 else null
                        return LoadResult.Page(articles.datas, prevKey, nextKey)
                    }
                } else {
                    // TODO: 临时用一个异常解决
                    return LoadResult.Error(IOException("${it.responseBody.errorMsg}"))
                }
            }
            .onException {
                return LoadResult.Error(it.exception)
            }
        return LoadResult.Error(IOException("未知错误"))
    }


    override fun getRefreshKey(state: PagingState<Int, ArticleBean>): Int? = null

}