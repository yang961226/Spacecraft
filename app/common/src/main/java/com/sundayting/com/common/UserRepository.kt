package com.sundayting.com.common

import com.sundayting.com.common.bean.UserBean
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteResource: UserRemoteResource,
    private val userLocalResource: UserLocalResource,
) {

    suspend fun login(username: String, password: String) =
        userRemoteResource.login(username, password)

    suspend fun logout() = userRemoteResource.logout()

    suspend fun getLocalUserBean(): UserBean? {
        return userLocalResource.getLocalUserBean()
    }

    suspend fun cacheUserBean(userBean: UserBean) {
        return userLocalResource.cacheUserBean(userBean)
    }

}