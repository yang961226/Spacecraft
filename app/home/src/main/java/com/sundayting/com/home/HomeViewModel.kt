package com.sundayting.com.home

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.bean.ArticleListBean
import com.sundayting.com.common.bean.BannerBean
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.home.article.ArticleRepository
import com.sundayting.com.home.banner.BannerRepository
import com.sundayting.com.network.onFailure
import com.sundayting.com.network.onSuccess
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bannerRepository: BannerRepository,
    private val articleRepository: ArticleRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val banner: MutableList<BannerBean> = arrayListOf(),
        val articleList: ArticleListBean? = null,
        val message: String? = null
    )

    init {
        refreshBanner()
        // TODO: 临时，后续会有改动
        refreshArticle(0)
    }

    private fun refreshBanner() {
        viewModelScope.launch {
            _uiState.update { uiState ->
                bannerRepository.getBanner().let {
                    if (it == null) {
                        uiState.copy(banner = arrayListOf())
                    } else {
                        uiState.copy(banner = it)
                    }
                }
            }
        }
    }

    private fun refreshArticle(page: Int) {
        viewModelScope.launch {
            // TODO: 先固定实现一个不翻页的
            articleRepository.getArticle(page)
                .onSuccess { response ->
                    response.responseBody.data?.let { articleList ->
                        _uiState.update { uiState ->
                            uiState.copy(articleList = articleList)
                        }
                    }
                }
                .onFailure {
                    _uiState.update { uiState ->
                        uiState.copy(message = "网络连接失败")
                    }
                }
        }
    }

    fun messageShown() {
        _uiState.update { uiState ->
            uiState.copy(message = null)
        }
    }

}