package com.sundayting.com.common.bean

data class ArticleBean(
    val id: Int = 0,

    /**
     * 作者
     */
    val author: String? = null,

    /**
     * 是否收藏
     */
    val collect: Boolean = false,

    /**
     * 描述信息
     */
    val desc: String? = null,

    /**
     * 图片类型，有和无
     */
    val picUrl: String? = null,

    /**
     * 链接
     */
    val link: String? = null,

    /**
     * 日期
     */
    val niceDate: String? = null,

    /**
     * 标题
     */
    val title: String? = null,

    /**
     * 文章标签
     */
    val superChapterName: String? = null,

    /**
     * 1.置顶
     */
    val topTitle: String? = null
)
