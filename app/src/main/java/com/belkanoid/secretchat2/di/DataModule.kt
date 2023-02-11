package com.belkanoid.secretchat2.di

import com.belkanoid.secretchat2.data.remote.ChatApi
import com.belkanoid.secretchat2.data.remote.ChatFactory
import com.belkanoid.secretchat2.data.repository.ChatRepositoryImpl
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    @Singleton
    fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    companion object {
        @Provides
        fun provideChatApi(): ChatApi = ChatFactory.service
    }
}