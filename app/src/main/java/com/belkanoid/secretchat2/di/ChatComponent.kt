package com.belkanoid.secretchat2.di

import android.content.Context
import com.belkanoid.secretchat2.presentation.screens.chatList.ChatListFragment
import com.belkanoid.secretchat2.presentation.screens.registration.RegistrationFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DataModule::class, ViewModelModule::class, DomainModule::class])
interface ChatComponent {

    fun inject(fragment: ChatListFragment)
    fun inject(fragment: RegistrationFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : ChatComponent
    }
}