package com.belkanoid.secretchat2.di

import androidx.lifecycle.ViewModel
import com.belkanoid.secretchat2.presentation.screens.chatList.viewModel.ChatListViewModel
import com.belkanoid.secretchat2.presentation.screens.registration.viewModel.RegistrationViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
interface ViewModelModule {

    @ViewModelKay(ChatListViewModel::class)
    @Binds
    @IntoMap
    fun bindsChatListViewModel(vm: ChatListViewModel): ViewModel

    @ViewModelKay(RegistrationViewModel::class)
    @Binds
    @IntoMap
    fun bindsRegistrationViewModel(vm: RegistrationViewModel): ViewModel

}

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKay(val key: KClass<out ViewModel>)

