package com.sundayting.com.home

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.article.ArticleRepository
import com.sundayting.com.common.bean.ArticleBean
import com.sundayting.com.common.bean.ArticleListBean
import com.sundayting.com.common.bean.BannerBean
import com.sundayting.com.common.dao.WanDatabase
import com.sundayting.com.common.widget.Tip
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.home.banner.BannerRepository
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
class HomeViewModel @Inject constructor(
    private val wanDatabase: WanDatabase,
    private val bannerRepository: BannerRepository,
    private val articleRepository: ArticleRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val swipeRefreshComplete: Boolean = false,
        val banner: List<BannerBean> = listOf(),
        val articleList: ArticleListBean? = null,
        val tipList: List<Tip> = listOf(),
        val loading: Boolean = false
    )

    init {
        refreshBanner()
        clearAndRefreshArticle()
    }

    fun collectArticle(id: Long) {
        viewModelScope.launch {
            if (wanDatabase.userDao().getUserLocal() == null) {
                _uiState.update { uiState ->
                    uiState.copy(tipList = uiState.tipList + Tip("请先登录"))
                }
                return@launch
            }
            _uiState.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.collectArticle(id)
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
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
                                tipList = uiState.tipList + Tip("收藏成功")
                            )
                        }
                    } else {
                        _uiState.update { uiState ->
                            uiState.copy(tipList = uiState.tipList + Tip("${it.responseBody.errorMsg}"))
                        }
                    }

                }
                .onFailure {
                    _uiState.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip("收藏失败")
                        )
                    }
                }
                .onFinish {
                    _uiState.update { uiState ->
                        uiState.copy(
                            loading = false
                        )
                    }
                }
        }
    }

    fun unCollectArticle(id: Long) {
        viewModelScope.launch {
            if (wanDatabase.userDao().getUserLocal() == null) {
                _uiState.update { uiState ->
                    uiState.copy(tipList = uiState.tipList + Tip("请先登录"))
                }
                return@launch
            }
            _uiState.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.unCollectArticle(id)
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
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
                                tipList = uiState.tipList + Tip("取消收藏成功")
                            )
                        }
                    } else {
                        _uiState.update { uiState ->
                            uiState.copy(tipList = uiState.tipList + Tip("${it.responseBody.errorMsg}"))
                        }
                    }

                }
                .onFailure {
                    _uiState.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip("取消收藏失败")
                        )
                    }
                }
                .onFinish {
                    _uiState.update { uiState ->
                        uiState.copy(
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

    fun clearAndRefreshArticle() {
        refreshArticle(0, true)
    }

    /**
     * 刷新文章列表
     * @param page Int 当前的页数
     * @param clear Boolean 是否清空之前的数据
     */
    private fun refreshArticle(page: Int, clear: Boolean = false) {
        if (clear) {
            _uiState.update { uiState ->
                uiState.copy(articleList = null)
            }
        }
        viewModelScope.launch {
            // TODO: 先固定实现一个不翻页的
            articleRepository.getArticle(page)
                .onSuccess { response ->
                    response.responseBody.data?.let { articleListBean ->
                        _uiState.update { uiState ->
                            uiState.copy(
                                articleList = articleListBean.copy(
                                    datas = articleListBean.datas + articleListBean.datas
                                )
                            )
                        }
                    }
                }
                .onFailure {
                    _uiState.update { uiState ->
                        uiState.copy(tipList = uiState.tipList + Tip("网络连接失败"))
                    }
                }
                .onFinish {
                    _uiState.update { uiState ->
                        uiState.copy(swipeRefreshComplete = true)
                    }
                }
        }
    }

    fun swipeRefreshCompleteKnown() {
        _uiState.update { uiState ->
            uiState.copy(swipeRefreshComplete = false)
        }
    }

}