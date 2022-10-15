package com.kunal.qrscanner.di

import android.content.Context
import com.kunal.qrscanner.data.datastore.DatastoreManager
import com.kunal.qrscanner.BaseApplication
import com.kunal.qrscanner.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): BaseApplication {
        return context as BaseApplication
    }

    @Singleton
    @Provides
    fun provideDatastoreManager(@ApplicationContext context: Context): DatastoreManager {
        return DatastoreManager(context)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.invoke(context)
    }
}