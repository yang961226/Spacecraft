package com.sundayting.com.mine

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.sundayting.com.common.article.ArticleAdapter
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
                .collect { articleListBean ->
                    if (articleListBean != null) {
                        articleAdapter.submitList(articleListBean.datas)
                    }
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

        binding.run {
            toolBar.ivBack.setOnClickListener { finish() }
            rvArticle.run {
                adapter = articleAdapter.also {
                    //解决recyclerView异步加载数据时的刷新问题
                    //http://www.zyiz.net/tech/detail-134593.html
                    it.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
            }
        }
    }

}