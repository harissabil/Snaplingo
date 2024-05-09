package com.harissabil.fisch.core.datastore.local_user_manager.di

import android.content.Context
import com.harissabil.fisch.core.datastore.local_user_manager.domain.DownloadModelManager
import com.igd.snaplingo.core.download_model_manager.data.DownloadModelManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DownloadModelManagerModule {

    @Provides
    @Singleton
    fun provideDownloadModelManager(
        @ApplicationContext context: Context,
    ): DownloadModelManager {
        return DownloadModelManagerImpl(context)
    }
}