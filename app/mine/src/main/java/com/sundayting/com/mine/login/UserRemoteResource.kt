package com.sundayting.com.mine.login

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class UserRemoteResource @Inject constructor(
    private val wanAppService: WanAppService
) {

    suspend fun login(username: String, password: String) = wanAppService.login(username, password)

}