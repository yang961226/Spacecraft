package com.sundayting.com.home.di

import androidx.recyclerview.widget.DiffUtil
import com.sundayting.com.common.bean.ArticleBean
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DiffModule {

    @Provides
    fun provideArticleItemCallback(): DiffUtil.ItemCallback<ArticleBean> {
        return object : DiffUtil.ItemCallback<ArticleBean>() {

            override fun areItemsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleBean, newItem: ArticleBean): Boolean {
                //只有点赞和时间可能存在改变
                return oldItem.collect == newItem.collect
                        && oldItem.niceDate == newItem.niceDate
            }

        }
    }

}