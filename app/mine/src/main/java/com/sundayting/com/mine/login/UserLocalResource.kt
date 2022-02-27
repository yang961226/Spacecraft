package com.sundayting.com.mine.login

import com.sundayting.com.common.bean.UserBean
import com.sundayting.com.common.dao.WanDatabase
import javax.inject.Inject

class UserLocalResource @Inject constructor(
    private val wanDatabase: WanDatabase,
) {

    suspend fun getLocalUserBean(): UserBean? {
        return wanDatabase.userDao().getUserLocal()
    }

    suspend fun cacheUserBean(userBean: UserBean) {
        wanDatabase.userDao().insertUser(userBean)
    }

}