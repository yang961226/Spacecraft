package com.sundayting.com.spacecraft.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.permissionx.guolindev.PermissionX
import com.sundayting.com.spacecraft.MainActivity
import com.sundayting.com.spacecraft.R
import com.sundayting.com.ui.BaseActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .map { it.directToNextActivity }
                    .distinctUntilChanged()
                    .collect { toNext ->
                        if (toNext) {
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }
                    }

            }
        }
        requestPermission()
    }

    private fun requestPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                val tips = "玩安卓现在要向您申请读取存储权限，用于访问您的本地音乐，您也可以在设置中手动开启或者取消。"
                scope.showRequestReasonDialog(deniedList, tips, "申请", "稍后")
            }
            .request { _, _, _ ->
                viewModel.hasPermission()
            }
    }

}