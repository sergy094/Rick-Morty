package com.sergiospinola.data.di

import com.sergiospinola.data.BuildConfig
import com.sergiospinola.data.network.RetrofitBuilder
import com.sergiospinola.data.network.RickMortyAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun glsConsigneeAPI(): RickMortyAPI =
        RetrofitBuilder(BuildConfig.API_URL.toHttpUrl(), BuildConfig.DEBUG).rickMortyAPI
}