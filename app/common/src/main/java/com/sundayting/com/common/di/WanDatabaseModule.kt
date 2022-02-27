package com.sundayting.com.common.di

import android.content.Context
import androidx.room.Room
import com.sundayting.com.common.dao.WanDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WanDatabaseModule {

    @Singleton
    @Provides
    fun provideWanDatabase(
        @ApplicationContext application: Context,
    ): WanDatabase {
        return Room.databaseBuilder(
            application,
            WanDatabase::class.java,
            "wan-database"
        ).build()
    }

}