package com.belkanoid.secretchat2.domain.repository

import com.belkanoid.secretchat2.domain.model.Message
import com.belkanoid.secretchat2.domain.model.Queue
import com.belkanoid.secretchat2.domain.model.User
import com.belkanoid.secretchat2.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun sendMessage(receiver: Long, sender: Long, message: String): Flow<Response<Boolean>>

    suspend fun createUser(userName: String): Flow<Response<Boolean>>

    suspend fun getMessage(messageId: Long): Response<Message>

    suspend fun getQueue(userId: Long): Response<List<Queue>>

    suspend fun getUser(userId: Long): Response<User>
}