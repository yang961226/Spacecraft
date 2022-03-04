package com.sundayting.com.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sundayting.com.common.UserViewModel
import com.sundayting.com.common.article.ArticlePagingAdapter
import com.sundayting.com.common.dao.WanDatabase
import com.sundayting.com.common.web.WebActivity
import com.sundayting.com.common.web.WebViewBean
import com.sundayting.com.common.widget.GlidePro
import com.sundayting.com.common.widget.NotificationHelper
import com.sundayting.com.home.databinding.FragmentHomeBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

//    @Inject
//    lateinit var articleAdapter: ArticleAdapter

    @Inject
    lateinit var articlePagingAdapter: ArticlePagingAdapter

    @Inject
    lateinit var wanDatabase: WanDatabase
    private val viewModel by viewModels<HomeViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
        initView()
    }

    private fun initView() {
        binding.run {
            // TODO: 临时关闭下拉刷新
            swipeRefreshLayout.isEnabled = false

            rvArticle.run {
                adapter = articlePagingAdapter.also {
                    it.onClickArticleItem = { clickedArticleItem, clickAction ->
                        when (clickAction) {
                            //当点击item时
                            ArticlePagingAdapter.ClickAction.NORMAL_CLICK -> {
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
                            //当点击收藏按钮时
                            ArticlePagingAdapter.ClickAction.COLLECT_CLICK -> {
                                if (clickedArticleItem.collect) {
                                    viewModel.unCollectArticle(clickedArticleItem.id)
                                } else {
                                    viewModel.collectArticle(clickedArticleItem.id)
                                }
                            }
                        }
                    }
                }
//                swipeRefreshLayout.setOnRefreshListener {
//                    viewModel.clearAndRefreshArticle()
//                }
                searchBar.ivAdd.setOnClickListener {
                    lifecycleScope.launchWhenCreated {
                        if (wanDatabase.userDao().getUserLocal() != null) {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPublishFragment())
                        } else {
                            notificationHelper.showTip("请登录后再尝试")
                        }
                    }
                }
            }
        }
    }

    private fun collectUiState() {

        launchAndRepeatWithViewLifecycle {
            viewModel.uiStateFlow
                .mapNotNull { it.articlePagingData }
                .distinctUntilChanged()
                .collect { pagingData ->
                    articlePagingAdapter.submitData(pagingData)
                }
        }

        //监听收藏进度
        // TODO: 发现一个问题，loading不止有状态，还应该要有loading的显示字段，后续修改为传回一个对象，包含两者
        launchAndRepeatWithViewLifecycle {
            viewModel.uiStateFlow
                .map { it.loading }
                .distinctUntilChanged()
                .collect { loading ->
                    if (loading) {
                        notificationHelper.showLoadingDialog("请稍后")
                    } else {
                        notificationHelper.dismissDialog()
                    }
                }
        }

        //监听tip
        launchAndRepeatWithViewLifecycle {
            viewModel.uiStateFlow
                .map { it.tipList }
                .distinctUntilChanged()
                .collect { tipList ->
                    tipList.firstOrNull()?.let { tip ->
                        notificationHelper.showTip(tip.content)
                        viewModel.tipShown(tip.uuid)
                    }
                }
        }

        //监听activity的UserBean，因为要监控是否用户退出了登陆，如果退出了登陆，就重新刷新列表数据（因为涉及收藏）
//        launchAndRepeatWithViewLifecycle {
//            userViewModel.uiState
//                .map { it.needRefreshHomeArticle }
//                .distinctUntilChanged()
//                .collect { updated ->
//                    if (updated) {
//                        viewModel.clearAndRefreshArticle()
//                        userViewModel.changeHomeArticleUpdateTag(false)
//                    }
//                }
//        }

        //监听Banner
        launchAndRepeatWithViewLifecycle {
            viewModel.uiStateFlow
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

        launchAndRepeatWithViewLifecycle {
            viewModel.uiStateFlow
                .map { it.swipeRefreshing }
                .distinctUntilChanged()
                .collect { swipeRefreshing ->
                    binding.swipeRefreshLayout.isRefreshing = swipeRefreshing
                }
        }

        //监听文章
//        launchAndRepeatWithViewLifecycle {
//            viewModel.uiStateFlow
//                .map { it.articleList }
//                .distinctUntilChanged()
//                .collect {
//                    articleAdapter.submitList(it)
//                }
//        }
    }

}