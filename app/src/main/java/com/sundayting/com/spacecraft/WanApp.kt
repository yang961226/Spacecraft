package com.sundayting.com.spacecraft

import com.kongzue.dialogx.DialogX
import com.sundayting.com.ui.BaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WanApp : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        DialogX.init(this)
        //允许同时弹出多个 PopTip
        DialogX.onlyOnePopTip = false
    }

}