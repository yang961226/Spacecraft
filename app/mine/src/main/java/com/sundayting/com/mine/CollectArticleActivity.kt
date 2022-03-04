package com.sundayting.com.mine

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kongzue.dialogx.dialogs.MessageDialog
import com.sundayting.com.common.article.ArticleAdapter
import com.sundayting.com.common.web.WebActivity
import com.sundayting.com.common.web.WebViewBean
import com.sundayting.com.common.widget.NotificationHelper
import com.sundayting.com.mine.collectarticle.CollectArticleViewModel
import com.sundayting.com.mine.databinding.ActivityCollectArticleBinding
import com.sundayting.com.ui.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class CollectArticleActivity : BaseBindingActivity<ActivityCollectArticleBinding>() {

    private val viewModel by viewModels<CollectArticleViewModel>()

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            viewModel.uiStateFlow
                .map { it.articleList }
                .distinctUntilChanged()
                .collect { articleBeanList ->
                    articleAdapter.submitList(articleBeanList)
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiStateFlow
                .map { it.loading }
                .distinctUntilChanged()
                .collect { loading ->
                    if (loading) {
                        notificationHelper.showLoadingDialog("加载中")
                    } else {
                        notificationHelper.dismissDialog()
                    }
                }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.uiStateFlow
                .map { it.swipeRefreshing }
                .distinctUntilChanged()
                .collect { swipeRefreshing ->
                    binding.swipeRefreshLayout.isRefreshing = swipeRefreshing
                }
        }

        binding.run {
            toolBar.run {
                ivBack.setOnClickListener { finish() }
                tvTitle.text = "我的收藏"
            }
            rvArticle.run {
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
                                        this@CollectArticleActivity,
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
                            //点击收藏
                            ArticleAdapter.ClickAction.COLLECT_CLICK -> {
                                MessageDialog.build()
                                    .setTitle("取消收藏")
                                    .setMessage("取消收藏文章：\n[${clickedArticleItem.title}]?")
                                    .setOkButton(
                                        "确定"
                                    ) { _, _ ->
                                        viewModel.unCollectArticle(clickedArticleItem.id)
                                        false
                                    }
                                    .setCancelButton("取消") { _, _ ->
                                        false
                                    }
                                    .show()
                            }
                        }
                    }
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.clearAndGetArticleCollected()
            }
        }
    }

}