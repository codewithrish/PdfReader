package com.codewithrish.pdfreader.core.analytics.di

import com.codewithrish.pdfreader.core.analytics.AnalyticsHelper
import com.codewithrish.pdfreader.core.analytics.AnalyticsHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: AnalyticsHelperImpl): AnalyticsHelper
}
