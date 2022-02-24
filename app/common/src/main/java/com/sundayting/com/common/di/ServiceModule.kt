package com.sundayting.com.common.di

import com.sundayting.com.common.WanAppService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Singleton
    @Provides
    fun provideWanAppService(
        retrofit: Retrofit
    ): WanAppService {
        return retrofit.create(WanAppService::class.java)
    }

}