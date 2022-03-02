package com.sundayting.com.mine.integral

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class IntegralRepository @Inject constructor(
    private val wanAppService: WanAppService
) {

    suspend fun getIntegralRecord(page: Long) = wanAppService.getIntegralRecord(page)

}