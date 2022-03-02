package com.sundayting.com.mine.di

import androidx.recyclerview.widget.DiffUtil
import com.sundayting.com.common.bean.IntegralBean
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DiffModule {

    @Provides
    fun provideIntegralItemCallback(): DiffUtil.ItemCallback<IntegralBean> {
        return object : DiffUtil.ItemCallback<IntegralBean>() {

            override fun areItemsTheSame(oldItem: IntegralBean, newItem: IntegralBean): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: IntegralBean, newItem: IntegralBean): Boolean {
                return true
            }

        }
    }

}