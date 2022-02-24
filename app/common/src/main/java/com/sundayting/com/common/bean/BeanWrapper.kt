package com.sundayting.com.common.bean

/**
 * 带壳的相应bean
 * @param T data实体类
 * @property data T 报文中对应data的部分
 * @property errorCode Int 报文中对应errorCode的部分
 * @property errorMsg String 报文中对应errorMsg的部分
 * @constructor
 */
data class BeanWrapper<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
) {

    /**
     * 请求是否成功
     * @return Boolean true:成功
     */
    fun isSuccessful(): Boolean {
        return errorCode == 0
    }

}