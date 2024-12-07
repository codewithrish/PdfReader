package com.codewithrish.pdfreader.core.analytics.di

import android.content.Context
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AnalyticsProvidersModule {
    // Firebase Analytics
    @Singleton
    @Provides
    fun provideFirebaseAnalytics() = Firebase.analytics
    // MixPanel Analytics
    @Singleton
    @Provides
    fun providesMixpanelAnalytics(
        @ApplicationContext context: Context
    ): MixpanelAPI {
        val trackAutomaticEvents = true
        val mixpanelProjectToken = "0ebf21d279eebdf835888017822d3761"
        return MixpanelAPI.getInstance(
            context,
            mixpanelProjectToken,
            trackAutomaticEvents
        )
    }
}