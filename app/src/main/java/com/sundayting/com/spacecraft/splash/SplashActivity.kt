package com.sundayting.com.spacecraft.splash

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.permissionx.guolindev.PermissionX
import com.sundayting.com.spacecraft.R
import com.sundayting.com.ui.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
                toNextActivity()
            }
    }

    private fun toNextActivity() = lifecycleScope.launch {
        delay(1000L)
        Toast.makeText(this@SplashActivity, "跳转", Toast.LENGTH_LONG).show()
//        startActivity(Intent(this@SplashActivity,))
    }

}