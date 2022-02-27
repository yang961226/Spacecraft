package com.sundayting.com.common.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Delete
    suspend fun delete(userBean: UserBean)

}