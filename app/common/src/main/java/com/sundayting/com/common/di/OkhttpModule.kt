package com.sundayting.com.common.di

import android.content.Context
import android.util.Log
import com.sundayting.com.network.okhttp.HttpLoggingInterceptorProMaxX
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.gotev.cookiestore.SharedPreferencesCookieStore
import net.gotev.cookiestore.okhttp.JavaNetCookieJar
import okhttp3.Cache
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
object OkhttpModule {

    @Qualifier
    annotation class LoggingInterceptor

    /**
     * 超时时间，10秒
     */
    private const val DEFAULT_TIMEOUT_MILLI = 1000 * 10L

    @Provides
    fun provideOkhttpClient(
        @LoggingInterceptor loggingInterceptor: Interceptor,
        cache: Cache,
        cookieJar: CookieJar
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(DEFAULT_TIMEOUT_MILLI, TimeUnit.MILLISECONDS)
            .connectTimeout(DEFAULT_TIMEOUT_MILLI, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_MILLI, TimeUnit.MILLISECONDS)
            .apply {
                addInterceptor(loggingInterceptor)
            }
            .cache(cache)
            .cookieJar(cookieJar)
            .build()
    }

    @Provides
    fun provideCache(
        @ApplicationContext applicationContext: Context
    ): Cache {
        return Cache(File(applicationContext.cacheDir, "cache"), 1024 * 1024 * 100L)
    }

    @LoggingInterceptor
    @Provides
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptorProMaxX(HttpLoggingInterceptor {
            Log.d("网络请求日志", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }

    @Provides
    fun provideCookieJar(
        @ApplicationContext applicationContext: Context
    ): CookieJar {
        val cookieStore = SharedPreferencesCookieStore(applicationContext, "wanAppCookieStore")
        val cookieManager = CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL)
        return JavaNetCookieJar(cookieManager)
    }

}