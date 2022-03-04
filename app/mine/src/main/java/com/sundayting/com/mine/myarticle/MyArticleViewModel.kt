package com.sundayting.com.mine.myarticle

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.common.widget.Tip
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.network.onFailure
import com.sundayting.com.network.onFinish
import com.sundayting.com.network.onSuccess
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyArticleViewModel @Inject constructor(
    private val myArticleRepository: MyArticleRepository
) : BaseViewModel() {

    private val _uiStateFlow = MutableStateFlow(UiState())
    val uiStateFlow = _uiStateFlow.immutable()

    data class UiState(
        val swipeRefreshing: Boolean = false,
        val tipList: List<Tip> = emptyList(),
        val articleBeanList: List<ArticleBean> = emptyList(),
    )

    init {
        clearAndRefreshMyArticle()
    }

    fun clearAndRefreshMyArticle() {
        getMyArticle(0, true)
    }

    fun tipShown(id: String) {
        _uiStateFlow.update { uiState ->
            uiState.copy(
                tipList = uiState.tipList.filterNot { id == it.uuid }
            )
        }
    }

    private fun getMyArticle(page: Long, clear: Boolean = false) {
        viewModelScope.launch {
            _uiStateFlow.update { uiState ->
                uiState.copy(
                    swipeRefreshing = clear
                )
            }
            myArticleRepository.getMySharedArticle(page)
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
                        it.responseBody.data?.shareArticles?.datas?.let { articleBeanList ->
                            _uiStateFlow.update { uiState ->
                                uiState.copy(
                                    articleBeanList = (if (clear) emptyList() else uiState.articleBeanList) + articleBeanList
                                )
                            }
                        }
                    } else {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(tipList = uiState.tipList + Tip("${it.responseBody.errorMsg}"))
                        }
                    }
                }
                .onFailure {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(tipList = uiState.tipList + Tip("$it"))
                    }
                }.onFinish {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(
                            swipeRefreshing = false
                        )
                    }
                }
        }
    }

}