package com.sundayting.com.mine.myarticle

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.sundayting.com.common.article.ArticleAdapter
import com.sundayting.com.common.web.WebActivity
import com.sundayting.com.common.web.WebViewBean
import com.sundayting.com.common.widget.NotificationHelper
import com.sundayting.com.mine.databinding.ActivityMyArticleBinding
import com.sundayting.com.ui.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MyArticleActivity : BaseBindingActivity<ActivityMyArticleBinding>() {
    @Inject
    lateinit var notificationHelper: NotificationHelper
    private val viewModel by viewModels<MyArticleViewModel>()

    @Inject
    lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiStateFlow
                    .map { it.articleBeanList }
                    .distinctUntilChanged()
                    .collect {
                        articleAdapter.submitList(
                            it.map { articleBean ->
                                articleBean.copy(
                                    collectVisible = false,
                                )
                            }
                        )
                    }
            }
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiStateFlow
                    .map { it.loadingState }
                    .distinctUntilChanged()
                    .collect { loadingState ->
                        if (loadingState.loading) {
                            notificationHelper.showLoadingDialog(loadingState.message)
                        } else {
                            notificationHelper.dismissDialog()
                        }
                    }
            }
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiStateFlow
                    .map { it.swipeRefreshFinished }
                    .distinctUntilChanged()
                    .collect { finished ->
                        if (finished) {
                            viewModel.swipeRefreshFinishedKnown()
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                    }
            }
        }

        binding.run {
            toolBar.run {
                swipeRefreshLayout.setOnRefreshListener {
                    viewModel.clearAndRefreshMyArticle()
                }
                tvTitle.text = "我的文章"
                ivBack.setOnClickListener { finish() }
                rvMyArticle.run {
                    adapter = articleAdapter.also {
                        //解决recyclerView异步加载数据时的刷新问题
                        //http://www.zyiz.net/tech/detail-134593.html
                        it.stateRestorationPolicy =
                            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                        it.onClickArticleItem = { clickedArticleItem, clickAction ->
                            when (clickAction) {
                                //当点击item时
                                ArticleAdapter.ClickAction.NORMAL_CLICK -> {
                                    startActivity(
                                        Intent(
                                            this@MyArticleActivity,
                                            WebActivity::class.java
                                        ).also { intent ->
                                            intent.putExtras(
                                                bundleOf(
                                                    "webViewBean" to WebViewBean(
                                                        loadUrl = clickedArticleItem.link,
                                                        title = clickedArticleItem.title
                                                    )
                                                )
                                            )
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}