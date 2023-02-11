package com.belkanoid.secretchat2.di

import android.content.Context
import com.belkanoid.secretchat2.presentation.chatList.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DataModule::class, ViewModelModule::class, DomainModule::class])
interface ChatComponent {


    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : ChatComponent
    }
}