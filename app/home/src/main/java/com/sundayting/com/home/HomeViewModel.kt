package com.sundayting.com.home

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.article.ArticleRepository
import com.sundayting.com.common.bean.ArticleBean
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

    private val _uiStateFlow = MutableStateFlow(UiState())
    val uiStateFlow = _uiStateFlow.immutable()

    data class UiState(
        val swipeRefreshing: Boolean = false,
        val banner: List<BannerBean> = listOf(),
        val articleList: List<ArticleBean> = emptyList(),
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
                _uiStateFlow.update { uiState ->
                    uiState.copy(tipList = uiState.tipList + Tip("请先登录"))
                }
                return@launch
            }
            _uiStateFlow.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.collectArticle(id)
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(
                                articleList = uiState.articleList.map { articleBean ->
                                    if (articleBean.id == id) {
                                        articleBean.copy(
                                            collect = true
                                        )
                                    } else {
                                        articleBean
                                    }
                                },
                                tipList = uiState.tipList + Tip("收藏成功")
                            )
                        }
                    } else {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(tipList = uiState.tipList + Tip("${it.responseBody.errorMsg}"))
                        }
                    }

                }
                .onFailure {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip("收藏失败")
                        )
                    }
                }
                .onFinish {
                    _uiStateFlow.update { uiState ->
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
                _uiStateFlow.update { uiState ->
                    uiState.copy(tipList = uiState.tipList + Tip("请先登录"))
                }
                return@launch
            }
            _uiStateFlow.update { uiState ->
                uiState.copy(loading = true)
            }
            articleRepository.unCollectArticle(id)
                .onSuccess { it ->
                    if (it.responseBody.isSuccessful()) {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(
                                articleList = uiState.articleList.map { articleBean ->
                                    if (articleBean.id == id) {
                                        articleBean.copy(
                                            collect = false
                                        )
                                    } else {
                                        articleBean
                                    }
                                },
                                tipList = uiState.tipList + Tip("取消收藏成功")
                            )
                        }
                    } else {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(tipList = uiState.tipList + Tip("${it.responseBody.errorMsg}"))
                        }
                    }

                }
                .onFailure {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip("取消收藏失败")
                        )
                    }
                }
                .onFinish {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(
                            loading = false
                        )
                    }
                }
        }
    }

    fun tipShown(tipId: String) {
        _uiStateFlow.update { uiState ->
            uiState.copy(tipList = uiState.tipList.filterNot { it.uuid == tipId })
        }
    }

    private fun refreshBanner() {
        viewModelScope.launch {
            _uiStateFlow.update { uiState ->
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
        viewModelScope.launch {
            _uiStateFlow.update { uiState ->
                uiState.copy(
                    swipeRefreshing = clear
                )
            }
            articleRepository.getArticle(page)
                .onSuccess { response ->
                    response.responseBody.data?.datas?.let { articleBeanList ->
                        _uiStateFlow.update { uiState ->
                            uiState.copy(
                                //如果是清空，则只添加新的list，如果不是清空，则把旧的也拼接上去
                                articleList = (if (clear) emptyList() else uiState.articleList) + articleBeanList
                            )
                        }
                    }
                }
                .onFailure {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(tipList = uiState.tipList + Tip("网络连接失败"))
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