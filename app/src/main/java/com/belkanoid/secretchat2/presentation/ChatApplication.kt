package com.belkanoid.secretchat2.presentation

import android.app.Application
import com.belkanoid.secretchat2.di.ChatComponent
import com.belkanoid.secretchat2.di.DaggerChatComponent

class ChatApplication: Application() {

    val component: ChatComponent by lazy {
        DaggerChatComponent.factory().create(applicationContext)
    }
}