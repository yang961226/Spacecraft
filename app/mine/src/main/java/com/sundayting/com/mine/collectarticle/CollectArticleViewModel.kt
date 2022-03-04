package com.sundayting.com.mine.collectarticle

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.article.ArticleRepository
import com.sundayting.com.common.bean.ArticleBean
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
        val swipeRefreshing: Boolean = false,
        val articleList: List<ArticleBean> = emptyList(),
        val loading: Boolean = false,
    )

    init {
        clearAndGetArticleCollected()
    }

    fun clearAndGetArticleCollected() {
        getArticleCollected(0, true)
    }

    fun unCollectArticle(id: Long) {
        viewModelScope.launch {
            _uiStateFlow.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.unCollectArticle(id)
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(loading = false)
                        }
                        clearAndGetArticleCollected()
                    }
                }
        }
    }

    private fun getArticleCollected(page: Int, clear: Boolean) {
        viewModelScope.launch {
            _uiStateFlow.update { uiState ->
                uiState.copy(
                    swipeRefreshing = clear,
                )
            }
            articleRepository.getArticleCollected(page)
                .onSuccess { response ->
                    if (response.responseBody.isSuccessful()) {
                        response.responseBody.data?.datas?.map {
                            it.copy(
                                collect = true,
                                id = it.originId
                            )
                        }?.let { articleBeanList ->
                            _uiStateFlow.update { uiState ->
                                uiState.copy(
                                    //如果是清空，则只添加新的list，如果不是清空，则把旧的也拼接上去
                                    articleList = (if (clear) emptyList() else uiState.articleList) + articleBeanList
                                )
                            }
                        }
                    }
                }
                .onFinish {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(swipeRefreshing = false)
                    }
                }
        }
    }

}