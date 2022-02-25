package com.sundayting.com.common.bean

data class BannerBean(
    val desc: String? = null,
    val id: Long = 0,
    val imagePath: String? = null,
    val isVisible: Int = 0,
    val order: Int = 0,
    val title: String? = null,
    val type: Int = 0,
    val url: String? = null
)