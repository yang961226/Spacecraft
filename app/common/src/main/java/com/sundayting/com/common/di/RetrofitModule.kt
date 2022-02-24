package com.sundayting.com.common.di

import com.sundayting.com.network.retrofit.adapter.ApexCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Qualifier
    annotation class BaseUrl

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory,
        @BaseUrl baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory {
        return ApexCallAdapterFactory.create()
    }

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String {
        return "https://www.wanandroid.com"
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return MoshiConverterFactory.create()
    }

}