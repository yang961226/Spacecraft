package com.sundayting.com.home.publish

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sundayting.com.common.widget.NotificationHelper
import com.sundayting.com.home.databinding.FragmentPublishBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class PublishFragment : BaseBindingFragment<FragmentPublishBinding>() {

    private val viewModel by viewModels<PublishViewModel>()

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //监听加载状态
        launchAndRepeatWithViewLifecycle {
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

        //监听提示
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

        //监听发表进度
        launchAndRepeatWithViewLifecycle {
            viewModel.uiStateFlow
                .map { it.publishSuccess }
                .distinctUntilChanged()
                .collect { publishSuccess ->
                    if (publishSuccess) {
                        findNavController().navigateUp()
                    }
                }
        }


        binding.run {
            toolBar.run {
                ivBack.setOnClickListener {
                    findNavController().navigateUp()
                }
                tvTitle.text = "发表文章"
            }
            btnPublish.setOnClickListener {
                viewModel.publish(etTitle.text.toString(), etLink.text.toString())
            }
        }
    }

}