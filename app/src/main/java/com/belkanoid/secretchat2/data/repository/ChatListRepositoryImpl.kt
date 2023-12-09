package com.belkanoid.secretchat2.data.repository

import com.belkanoid.secretchat2.data.mapper.toMessage
import com.belkanoid.secretchat2.data.mapper.toQueue
import com.belkanoid.secretchat2.data.remote.ChatApi
import com.belkanoid.secretchat2.data.rsa.RsaKey
import com.belkanoid.secretchat2.domain.model.Message
import com.belkanoid.secretchat2.domain.model.Queue
import com.belkanoid.secretchat2.domain.repository.ChatListRepository
import com.belkanoid.secretchat2.domain.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ChatListRepositoryImpl @Inject constructor(
    private val service: ChatApi,
    private val rsaKey: RsaKey,
    @Named("token") private val  token: String,
    @Named("privateKey") private val  privateKey: String,
    @Named("userId") private val  currentUserId: Long,
) : ChatListRepository {

    private val _messages = MutableSharedFlow<List<Message>>()
    override val messages = _messages.asSharedFlow()

    override suspend fun refreshMessageList(oldMessagesId: List<Long>) {
        getQueue()
            .map { response ->
                when (response) {
                    is Response.Success -> response.data?.map { it.messageId }
                    is Response.Error -> throw RuntimeException(response.message)
                }
            }
            .catch { _messages.emit(listOf()) }
            .filterNotNull()
            .map { it.subtract(oldMessagesId.toSet()) }
            .collect { messagesId ->
                _messages.emit(messagesId.map { getMessage(it) })
            }
    }

    private suspend fun getMessage(messageId: Long): Message {
        val message = service.getMessage(messageId, token).toMessage()
        val decryptedMessage = rsaKey.decryptMessage(message.message, privateKey)
        return message.copy(message = decryptedMessage)
    }

    private suspend fun getQueue(): Flow<Response<List<Queue>>> = flow {
        try {
            val queue = service.getQueue(currentUserId, token).map { it.toQueue() }
            emit(Response.Success(data = queue))
        } catch (e: Exception) {
            emit(Response.Error(message = "Could not get queue for: $currentUserId"))
        }
    }


}

