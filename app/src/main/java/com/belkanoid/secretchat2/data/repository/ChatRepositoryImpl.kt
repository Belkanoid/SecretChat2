package com.belkanoid.secretchat2.data.repository

import com.belkanoid.secretchat2.data.mapper.toMessage
import com.belkanoid.secretchat2.data.mapper.toQueue
import com.belkanoid.secretchat2.data.mapper.toUser
import com.belkanoid.secretchat2.data.remote.ChatApi
import com.belkanoid.secretchat2.data.remote.dto.MessageDto
import com.belkanoid.secretchat2.data.remote.dto.UserDto
import com.belkanoid.secretchat2.data.remote.postBody.MessageBody
import com.belkanoid.secretchat2.data.remote.postBody.UserBody
import com.belkanoid.secretchat2.data.rsa.RsaKey
import com.belkanoid.secretchat2.domain.model.Message
import com.belkanoid.secretchat2.domain.model.Queue
import com.belkanoid.secretchat2.domain.model.User
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import com.belkanoid.secretchat2.domain.util.Response
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.PRIVATE_KEY
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.PUBLIC_KEY
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.TOKEN
import com.belkanoid.secretchat2.domain.util.SharedPreferences.Companion.USER_ID
import com.belkanoid.secretchat2.domain.util.log
import com.belkanoid.secretchat2.domain.util.rethrowCancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChatRepositoryImpl @Inject constructor(
    private val service: ChatApi,
    private val rsaKey: RsaKey,
    private val sharedPreferences: SharedPreferences,
) : ChatRepository {
    override suspend fun sendMessage(
        receiver: Long,
        sender: Long,
        message: String
    ): Boolean = withContext(Dispatchers.IO) {
        val receiverPublicKey =
            getUser(receiver).data?.PublicKey ?: throw RuntimeException("receiverPublicKey is null")
        val encryptedMessage = rsaKey.encryptMessage(message, receiverPublicKey)

        val messageBody = MessageBody(
            receiver = receiver,
            sender = sender,
            message = encryptedMessage,
            timestamp = Date().time,
            isEdited = false
        )
        suspendCancellableCoroutine { cancellableContinuation ->
            launch(Dispatchers.IO) {
                service.sendMessage(postMessageBody = messageBody).enqueue(
                    object : retrofit2.Callback<MessageDto> {
                        override fun onResponse(
                            call: Call<MessageDto>,
                            response: retrofit2.Response<MessageDto>
                        ) {
                            cancellableContinuation.resume(true)
                        }

                        override fun onFailure(call: Call<MessageDto>, t: Throwable) {
                            cancellableContinuation.resume(false)
                        }
                    }
                )
            }
        }
    }

    override suspend fun createUser(
        userName: String
    ): Boolean = withContext(Dispatchers.IO) {
        val token = UUID.randomUUID().toString().replace("-", "")
        val publicKey = sharedPreferences.getString(PUBLIC_KEY)
        sharedPreferences.putString(TOKEN, token)
        log("ChatRepository", "token for $userName is generated")

        val userBody = UserBody(
            name = userName,
            pkey = publicKey,
            token = token
        )
        suspendCancellableCoroutine { cancellableContinuation ->
            launch(Dispatchers.IO) {
                service.createUser(postUserBody = userBody).enqueue(
                    object : retrofit2.Callback<UserDto> {
                        override fun onResponse(
                            call: Call<UserDto>,
                            response: retrofit2.Response<UserDto>
                        ) {
                            val userId = response.body()?.id ?: -1L
                            sharedPreferences.putLong(value = userId)
                            cancellableContinuation.resume(true)
                        }

                        override fun onFailure(call: Call<UserDto>, t: Throwable) {
                            cancellableContinuation.resume(false)
                        }
                    }
                )
            }
        }
    }

    override suspend fun getMessages(oldMessagesId: List<Long>): List<Response<Message>> = withContext(Dispatchers.IO) {
        try {
            val currentMessagesId = getQueue().data?.map { it.messageId } ?: listOf()
            val newMessagesId = currentMessagesId.subtract(oldMessagesId.toSet())
            newMessagesId.map { getMessage(it) }//Lis<Response<Message>>
        }catch (e: Exception) {
            e.rethrowCancellationException()
            log("Chat repository", e.message ?: "Could not get Messages")
            listOf(Response.Error("Could not get Messages"))
        }
    }

    private suspend fun getMessage(messageId: Long): Response<Message> = withContext(Dispatchers.IO) {
        val token = sharedPreferences.getString(TOKEN)
        val privateKey = sharedPreferences.getString(PRIVATE_KEY)
        try {
            val message = service.getMessage(messageId, token).toMessage()
            val decryptedMessage = rsaKey.decryptMessage(message.message, privateKey)
            Response.Success(
                data = message.copy(message = decryptedMessage)
            )
        }catch (e: Exception) {
            e.rethrowCancellationException()
            log("Chat repository", e.message ?: "Could not get message with $messageId id")
            Response.Error("Could not get message with $messageId id")
        }
    }

    private suspend fun getQueue(): Response<List<Queue>> = withContext(Dispatchers.IO) {
            val currentUserId = sharedPreferences.getLong()
            val token = sharedPreferences.getString(TOKEN)
            try {
                Response.Success(
                    data = service.getQueue(currentUserId, token).map { it.toQueue() }
                )
            } catch (e: Exception) {
                e.rethrowCancellationException()
                log("ChatRepository", e.message ?: "Could not get Queue for: $currentUserId")
                Response.Error("Could not get Queue for: $currentUserId")
            }
        }

    override suspend fun getUser(userId: Long): Response<User> = withContext(Dispatchers.IO) {
        try {
            Response.Success(
                data = service.getUser(userId).toUser()
            )
        } catch (e: Exception) {
            e.rethrowCancellationException()
            log("ChatRepository", e.message ?: "Could not get User: $userId")
            Response.Error("Could not get User: $userId")
        }
    }
}

