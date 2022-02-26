package com.sundayting.com.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sundayting.com.common.web.WebActivity
import com.sundayting.com.common.web.WebViewBean
import com.sundayting.com.common.widget.GlidePro
import com.sundayting.com.home.article.ArticleAdapter
import com.sundayting.com.home.databinding.FragmentHomeBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var articleAdapter: ArticleAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
        binding.rvArticle.run {
            adapter = articleAdapter.also {
                //解决recyclerView异步加载数据时的刷新问题
                //http://www.zyiz.net/tech/detail-134593.html
                it.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                it.onClickArticleItem = { clickedArticleItem, clickAction ->
                    when (clickAction) {
                        ArticleAdapter.ClickAction.NORMAL_CLICK -> {
                            startActivity(
                                Intent(
                                    requireActivity(),
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
                        ArticleAdapter.ClickAction.COLLECT_CLICK -> {

                        }
                    }

                }
            }
        }
    }

    private fun collectUiState() {
        //监听Banner
        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .map { it.banner }
                .distinctUntilChanged()
                .collect { beanList ->
                    // TODO: 目前只响应一个 ，后面改造成轮播
                    beanList.firstOrNull()?.let { bean ->
                        GlidePro.withViewLifecycleOwner(this@HomeFragment)
                            .load(bean.imagePath)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.ivBanner)
                    }

                }
        }

        //监听文章
        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .map { it.articleList }
                .distinctUntilChanged()
                .collect {
                    it?.datas?.let { articleBeanList ->
                        articleAdapter.submitList(articleBeanList)
                    }
                }
        }
    }

}