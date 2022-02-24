package com.sundayting.com.common.di

import com.sundayting.com.common.widget.WaitDialogHelper
import com.sundayting.com.common.widget.WaitDialogHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class WaitDialogHelperModule {

    @Singleton
    @Binds
    abstract fun bindsWaitDialogHelper(impl: WaitDialogHelperImpl): WaitDialogHelper

}