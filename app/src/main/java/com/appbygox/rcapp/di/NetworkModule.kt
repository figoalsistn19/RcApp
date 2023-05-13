package com.appbygox.rcapp.di

import com.appbygox.rcapp.data.remote.FirestoreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideFirestoreModule() = FirestoreService()
}