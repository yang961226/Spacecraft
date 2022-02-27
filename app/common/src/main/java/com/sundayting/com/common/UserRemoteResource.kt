package com.sundayting.com.common

import javax.inject.Inject

class UserRemoteResource @Inject constructor(
    private val wanAppService: WanAppService
) {

    suspend fun login(username: String, password: String) = wanAppService.login(username, password)

    suspend fun logout() = wanAppService.logout()

}