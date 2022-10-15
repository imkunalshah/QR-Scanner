package com.kunal.qrscanner.di

import com.kunal.qrscanner.data.repository.HistoryRepository
import com.kunal.qrscanner.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideHistoryRepository(
        appDatabase: AppDatabase
    ): HistoryRepository {
        return HistoryRepository(appDatabase)
    }

}