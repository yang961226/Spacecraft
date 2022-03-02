package com.sundayting.com.common.bean

data class ListBean<T>(
    val curPage: Int = 0,
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0,
    val datas: List<T> = emptyList()
)