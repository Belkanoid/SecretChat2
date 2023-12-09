package com.belkanoid.secretchat2.domain.repository

import com.belkanoid.secretchat2.domain.model.User
import com.belkanoid.secretchat2.domain.util.Response
import kotlinx.coroutines.flow.Flow

interface NewMessageRepository {

    suspend fun sendMessage(receiver: Long, sender: Long, message: String): Flow<Boolean>

    suspend fun getUser(userId: Long): Flow<Response<User>>

}