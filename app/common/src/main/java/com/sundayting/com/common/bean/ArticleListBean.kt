package com.sundayting.com.common.bean

@Deprecated("废弃", replaceWith = ReplaceWith("替换ListBean"))
data class ArticleListBean(
    val curPage: Long = 0,
    val offset: Long = 0,
    val over: Boolean = false,
    val pageCount: Long = 0,
    val size: Long = 0,
    val total: Long = 0,
    // TODO: 起别名失效，准备探究原因 
    val datas: List<ArticleBean> = emptyList()
)