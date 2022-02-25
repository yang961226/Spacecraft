package com.sundayting.com.home.banner

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class BannerRemoteResource @Inject constructor(
    private val wanAppService: WanAppService
) {

    suspend fun getBanner() = wanAppService.banner()

}