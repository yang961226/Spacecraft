package com.sundayting.com.common.bean

data class ArticleBean(
    //这里的是接口属性
    val apkLink: String = "",
    val audit: Long = 0,
    val author: String = "",
    val canEdit: Boolean = false,
    val chapterId: Long = 0,
    val chapterName: String = "",
    val collect: Boolean = false,
    val courseId: Long = 0,
    val desc: String = "",
    val descMd: String = "",
    val envelopePic: String = "",
    val fresh: Boolean = false,
    val id: Long = 0,
    val link: String = "",
    val niceDate: String = "",
    val niceShareDate: String = "",
    val origin: String = "",
    val prefix: String = "",
    val projectLink: String = "",
    val publishTime: Long = 0,
    val selfVisible: Long = 0,
    val shareDate: Long = 0,
    val shareUser: String = "",
    val superChapterId: Long = 0,
    val superChapterName: String = "",
    val title: String = "",
    val type: Long = 0,
    val userId: Long = 0,
    val visible: Long = 0,
    val zan: Long = 0,
//    val tags: List<*>? = null,

    //这里的是自定义属性
    val isTop: Boolean = false,

    //收藏文章专属
    val originId: Long = 0,
)
