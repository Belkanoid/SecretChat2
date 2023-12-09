package com.belkanoid.secretchat2.domain.repository

import com.belkanoid.secretchat2.domain.model.Message
import kotlinx.coroutines.flow.SharedFlow

interface ChatListRepository {

    val messages: SharedFlow<List<Message>>

    suspend fun refreshMessageList(oldMessagesId: List<Long>)



}