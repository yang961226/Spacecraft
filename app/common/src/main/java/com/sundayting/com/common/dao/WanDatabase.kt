package com.sundayting.com.common.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sundayting.com.common.bean.UserBean

@Database(entities = [UserBean::class], version = 1)
abstract class WanDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}