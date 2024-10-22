package com.baimstask.di

import android.content.Context
import com.baimstask.data.local.AssetManager
import com.baimstask.data.local.DefaultFileDataSource
import com.baimstask.data.local.FileDataSource
import com.baimstask.util.AppDispatchers
import com.baimstask.util.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FileDatasourceModule {

    @Provides
    @Singleton
    internal fun providesDefaultFileDataSource(
        assetManager: AssetManager,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): FileDataSource = DefaultFileDataSource(assetManager, ioDispatcher)


    @Provides
    @Singleton
    fun providesAssetManager(
        @ApplicationContext context: Context,
    ): AssetManager = AssetManager(context.assets::open)

}