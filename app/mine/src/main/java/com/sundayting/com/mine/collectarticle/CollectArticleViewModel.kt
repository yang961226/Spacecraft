package com.sundayting.com.mine.collectarticle

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.article.ArticleRepository
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.common.bean.ArticleListBean
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.network.onFinish
import com.sundayting.com.network.onSuccess
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : BaseViewModel() {

    private val _uiStateFlow = MutableStateFlow(UiState())
    val uiStateFlow = _uiStateFlow.immutable()

    data class UiState(
        val finishSwipeRefreshLoading: Boolean = false,
        val articleList: ArticleListBean? = null,
        val loading: Boolean = false,
    )

    init {
        clearAndGetArticleCollected()
    }

    fun clearAndGetArticleCollected() {
        getArticleCollected(0, true)
    }

    fun swipeRefreshComplete() {
        _uiStateFlow.update { uiState ->
            uiState.copy(finishSwipeRefreshLoading = false)
        }
    }

    fun unCollectArticle(id: Long) {
        viewModelScope.launch {
            articleRepository.unCollectArticle(id.toLong())
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
                        clearAndGetArticleCollected()
                    }
                }
        }
    }

    private fun getArticleCollected(page: Int, clear: Boolean) {
        viewModelScope.launch {
            _uiStateFlow.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.getArticleCollected(page)
                // TODO: 部分失败逻辑暂时不写，懒
                .onSuccess { response ->
                    if (response.responseBody.isSuccessful()) {
                        response.responseBody.data?.let { articleListBean ->

                            // TODO: 后面打算建立一个专门用于已收藏的实体类，因为已收藏的接口提供的信息少一点
                            //全部变成已收藏（因为接口没提供已收藏的字段）
                            articleListBean.copy(
                                datas = mutableListOf<ArticleBean>().apply {
                                    articleListBean.datas.forEach {
                                        this.add(it.copy(collect = true))
                                    }
                                }
                            ).let { newArticleListBean ->
                                _uiStateFlow.update { uiState ->
                                    uiState.copy(
                                        //如果是清除模式，则只引入最新一次加载的数据，如果不是，则把之前的也一起加进去
                                        articleList = if (clear) newArticleListBean else newArticleListBean.copy(
                                            datas = (uiState.articleList?.datas
                                                ?: emptyList()) + newArticleListBean.datas
                                        ),
                                        finishSwipeRefreshLoading = true
                                    )
                                }
                            }


                        }
                    }
                }
                .onFinish {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(loading = false)
                    }
                }
        }
    }

}