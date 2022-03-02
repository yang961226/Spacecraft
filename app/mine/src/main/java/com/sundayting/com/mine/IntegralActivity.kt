package com.sundayting.com.mine

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.sundayting.com.common.widget.NotificationHelper
import com.sundayting.com.mine.databinding.ActivityIntegralBinding
import com.sundayting.com.mine.integral.IntegralAdapter
import com.sundayting.com.mine.integral.IntegralViewModel
import com.sundayting.com.ui.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class IntegralActivity : BaseBindingActivity<ActivityIntegralBinding>() {

    private val viewModel by viewModels<IntegralViewModel>()

    @Inject
    lateinit var integralAdapter: IntegralAdapter

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        collectUiState()
    }

    private fun collectUiState() {
        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiStateFlow
                    .map { it.swipeLoading }
                    .distinctUntilChanged()
                    .collect { swipeLoading ->
                        binding.swipeRefreshLayout.isRefreshing = swipeLoading
                    }
            }
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiStateFlow
                    .map { it.integralBeanList }
                    .distinctUntilChanged()
                    .collect { integralList ->
                        integralAdapter.submitList(integralList)
                    }
            }
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
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
        }
    }

    private fun initView() {
        binding.run {
            toolBar.run {
                ivBack.setOnClickListener { finish() }
                tvTitle.text = "我的积分"
            }
            rvIntegral.run {
                adapter = integralAdapter.also {
                    //解决recyclerView异步加载数据时的刷新问题
                    //http://www.zyiz.net/tech/detail-134593.html
                    it.stateRestorationPolicy =
                        RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.clearAndRefreshIntegralRecord()
            }
        }
    }

}