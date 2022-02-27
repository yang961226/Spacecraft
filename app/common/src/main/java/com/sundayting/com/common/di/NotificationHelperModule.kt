package com.sundayting.com.common.di

import com.sundayting.com.common.widget.NotificationHelper
import com.sundayting.com.common.widget.NotificationHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NotificationHelperModule {

    @Singleton
    @Binds
    abstract fun bindsNotificationHelper(impl: NotificationHelperImpl): NotificationHelper

}