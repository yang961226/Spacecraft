package com.sundayting.com.home

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.common.bean.ArticleListBean
import com.sundayting.com.common.bean.BannerBean
import com.sundayting.com.common.widget.Tip
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
        val banner: List<BannerBean> = listOf(),
        val articleList: ArticleListBean? = null,
        val message: String? = null,
        val tipList: List<Tip> = listOf(),
        val loading: Boolean = false
    )

    init {
        refreshBanner()
        // TODO: 临时，后续会有改动
        refreshArticle(0)
    }

    fun collectArticle(id: Long) {
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.collectArticle(id)
                .onSuccess {
                    _uiState.update { uiState ->
                        uiState.copy(
                            articleList = uiState.articleList?.copy(
                                datas = uiState.articleList.datas.let { articleBeanList ->
                                    val newArticleBeanList = mutableListOf<ArticleBean>()
                                    for (articleBean in articleBeanList) {
                                        if (articleBean.id == id) {
                                            newArticleBeanList.add(articleBean.copy(collect = true))
                                        } else {
                                            newArticleBeanList.add(articleBean)
                                        }
                                    }
                                    newArticleBeanList
                                }
                            ),
                            tipList = uiState.tipList + Tip("收藏成功"),
                            loading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip("收藏失败"),
                            loading = false
                        )
                    }
                }
        }
    }

    fun unCollectArticle(id: Long) {
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.unCollectArticle(id)
                .onSuccess {
                    _uiState.update { uiState ->
                        uiState.copy(
                            articleList = uiState.articleList?.copy(
                                datas = uiState.articleList.datas.let { articleBeanList ->
                                    val newArticleBeanList = mutableListOf<ArticleBean>()
                                    for (articleBean in articleBeanList) {
                                        if (articleBean.id == id) {
                                            newArticleBeanList.add(articleBean.copy(collect = false))
                                        } else {
                                            newArticleBeanList.add(articleBean)
                                        }
                                    }
                                    newArticleBeanList
                                }
                            ),
                            tipList = uiState.tipList + Tip("取消收藏成功"),
                            loading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip("取消收藏失败"),
                            loading = false
                        )
                    }
                }
        }
    }

    fun tipShown(tipId: String) {
        _uiState.update { uiState ->
            uiState.copy(tipList = uiState.tipList.filterNot { it.uuid == tipId })
        }
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