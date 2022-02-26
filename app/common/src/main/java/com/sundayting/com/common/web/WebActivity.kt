package com.sundayting.com.common.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sundayting.com.common.databinding.ActivityWebBinding
import com.sundayting.com.ui.BaseBindingActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class WebActivity : BaseBindingActivity<ActivityWebBinding>() {

    private val viewModel by viewModels<WebViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        //监听进度条可见度
        lifecycleScope.launchWhenCreated {
            viewModel.uiState
                .map { it.progressBarVisible }
                .distinctUntilChanged()
                .collect { visible ->
                    binding.progressBar.visibility = if (visible) View.VISIBLE else View.GONE
                }
        }

        //监听进度条进度
        lifecycleScope.launchWhenCreated {
            viewModel.uiState
                .map { it.progress }
                .distinctUntilChanged()
                .collect { progress ->
                    binding.progressBar.progress = progress
                }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        binding.run {
            ivBack.setOnClickListener { finish() }

            webView.run {
                settings.run {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                }
                //如果不设置WebViewClient，请求会跳转系统浏览器
                webViewClient = object : WebViewClient() {}

                //监听加载进度
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        viewModel.updateProgress(newProgress)
                    }
                }

                (intent.getSerializableExtra("webViewBean") as? WebViewBean)?.let { webViewBean ->
                    loadUrl(webViewBean.loadUrl)
                }
            }

            onBackPressedDispatcher.addCallback(this@WebActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (binding.webView.canGoBack()) {
                            binding.webView.goBack()
                        } else {
                            finish()
                        }
                    }
                })
        }
    }

}