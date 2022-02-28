package com.sundayting.com.mine.collectarticle

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.article.ArticleRepository
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
        val articleList: ArticleListBean? = null,
        val loading: Boolean = false,
    )

    init {
        getArticleCollected(0)
    }

    private fun getArticleCollected(page: Int) {
        viewModelScope.launch {
            _uiStateFlow.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.getArticleCollected(page)
                // TODO: 部分失败逻辑暂时不写，懒
                .onSuccess { response ->
                    if (response.responseBody.isSuccessful()) {
                        response.responseBody.data?.let { articleListBean ->
                            _uiStateFlow.update { uiState ->
                                uiState.copy(
                                    articleList = articleListBean.copy(
                                        datas = articleListBean.datas + articleListBean.datas
                                    )
                                )
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