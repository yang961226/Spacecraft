package com.sundayting.com.common.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "userBean")
data class UserBean(
    @PrimaryKey val id: Long = 0,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "rank") val rank: Long = 0,
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "nick_name") val nickname: String? = null,
    @ColumnInfo(name = "password") val password: String? = null,
    @ColumnInfo(name = "public_name") val publicName: String? = null,
    @ColumnInfo(name = "token") val token: String? = null,
    @ColumnInfo(name = "type") val type: Int = 0,
    @ColumnInfo(name = "username") val username: String? = null,
    @ColumnInfo(name = "coin_count") val coinCount: Int = 0
) : Serializable