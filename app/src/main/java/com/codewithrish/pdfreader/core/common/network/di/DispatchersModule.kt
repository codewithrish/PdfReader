package com.codewithrish.pdfreader.core.common.network.di

import com.codewithrish.pdfreader.core.common.network.Dispatcher
import com.codewithrish.pdfreader.core.common.network.CwrDispatchers.IO
import com.codewithrish.pdfreader.core.common.network.CwrDispatchers.Default
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}