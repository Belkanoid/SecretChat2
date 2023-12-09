package com.belkanoid.secretchat2.di

import com.belkanoid.secretchat2.data.remote.ChatApi
import com.belkanoid.secretchat2.data.remote.ChatFactory
import com.belkanoid.secretchat2.data.repository.ChatListRepositoryImpl
import com.belkanoid.secretchat2.data.repository.NewMessageRepositoryImpl
import com.belkanoid.secretchat2.data.repository.NewUserRepositoryImpl
import com.belkanoid.secretchat2.domain.repository.ChatListRepository
import com.belkanoid.secretchat2.domain.repository.NewMessageRepository
import com.belkanoid.secretchat2.domain.repository.NewUserRepository
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.PRIVATE_KEY
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.TOKEN
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    @Singleton
    fun bindChatListRepository(chatListRepositoryImpl: ChatListRepositoryImpl): ChatListRepository

    @Binds
    @Singleton
    fun bindNewMessageRepository(newMessageRepositoryImpl: NewMessageRepositoryImpl): NewMessageRepository

    @Binds
    @Singleton
    fun bindNewUserRepository(newUserRepositoryImpl: NewUserRepositoryImpl): NewUserRepository


    companion object {
        @Provides
        fun provideChatApi(): ChatApi = ChatFactory.service

        @Provides
        @Named("token")
        fun provideToken(sharedPreferences: SharedPreferences): String =
            sharedPreferences.getString(TOKEN)

        @Provides
        @Named("privateKey")
        fun providePrivateKey(sharedPreferences: SharedPreferences): String =
            sharedPreferences.getString(PRIVATE_KEY)

        @Provides
        @Named("userId")
        fun provideUserId(sharedPreferences: SharedPreferences): Long =
            sharedPreferences.getLong()
    }
}