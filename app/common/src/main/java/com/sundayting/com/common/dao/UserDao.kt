package com.sundayting.com.common.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.sundayting.com.common.bean.UserBean

@Dao
interface UserDao {

    @Query("SELECT * FROM userBean")
    suspend fun getUserLocal(): UserBean?

    @Insert(onConflict = REPLACE)
    suspend fun insertUser(userBean: UserBean)

    @Query("DELETE FROM userBean")
    suspend fun clearUser()

}