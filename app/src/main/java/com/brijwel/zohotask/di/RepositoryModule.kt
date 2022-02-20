package com.brijwel.zohotask.di

import com.brijwel.zohotask.data.local.PostDao
import com.brijwel.zohotask.data.network.ApiService
import com.brijwel.zohotask.data.repository.PostRepositoryImpl
import com.brijwel.zohotask.domain.repository.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun providePostRepository(apiService: ApiService, postDao: PostDao): PostRepository {
        return PostRepositoryImpl(apiService, postDao)
    }
}