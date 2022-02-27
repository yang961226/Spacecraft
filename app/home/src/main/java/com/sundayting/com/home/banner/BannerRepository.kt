package com.sundayting.com.home.banner

import com.sundayting.com.common.bean.BannerBean
import com.sundayting.com.network.onSuccess
import javax.inject.Inject

class BannerRepository @Inject constructor(
    private val bannerLocalResource: BannerLocalResource,
    private val bannerRemoteResource: BannerRemoteResource
) {

    suspend fun getBanner(): List<BannerBean>? {
        bannerRemoteResource.getBanner().onSuccess {
            if (it.responseBody.isSuccessful()) {
                return it.responseBody.data
            }
        }
        return null
    }

}