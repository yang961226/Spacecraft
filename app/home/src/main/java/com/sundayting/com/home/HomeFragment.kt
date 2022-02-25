package com.sundayting.com.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sundayting.com.common.widget.GlidePro
import com.sundayting.com.home.databinding.FragmentHomeBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

}