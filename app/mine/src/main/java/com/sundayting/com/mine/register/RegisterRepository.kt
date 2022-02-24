package com.sundayting.com.mine.register

import com.sundayting.com.common.WanAppService
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val wanAppService: WanAppService
) {

    suspend fun register(username: String, password: String, rePassword: String) =
        wanAppService.register(username, password, rePassword)

}