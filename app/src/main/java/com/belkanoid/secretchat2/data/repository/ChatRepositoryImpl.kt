package com.belkanoid.secretchat2.data.repository

import com.belkanoid.secretchat2.data.mapper.toMessage
import com.belkanoid.secretchat2.data.mapper.toQueue
import com.belkanoid.secretchat2.data.mapper.toUser
import com.belkanoid.secretchat2.data.remote.ChatApi
import com.belkanoid.secretchat2.data.remote.dto.MessageDto
import com.belkanoid.secretchat2.data.remote.dto.UserDto
import com.belkanoid.secretchat2.data.remote.postBody.MessageBody
import com.belkanoid.secretchat2.data.remote.postBody.UserBody
import com.belkanoid.secretchat2.domain.model.Message
import com.belkanoid.secretchat2.domain.model.Queue
import com.belkanoid.secretchat2.domain.model.User
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import com.belkanoid.secretchat2.domain.util.Response
import com.belkanoid.secretchat2.domain.util.log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import java.util.*
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val service: ChatApi
) : ChatRepository {
    override suspend fun sendMessage(
        receiver: Long,
        sender: Long,
        message: String
    ): Flow<Response<Boolean>> {
        val messageBody = MessageBody(
            receiver = receiver,
            sender = sender,
            message = message,
            timestamp = Date().time,
            isEdited = false
        )
        return callbackFlow {
            service.sendMessage(postMessageBody = messageBody).enqueue(
                object : retrofit2.Callback<MessageDto> {
                    override fun onResponse(
                        call: Call<MessageDto>,
                        response: retrofit2.Response<MessageDto>
                    ) {
                        launch { send(Response.Success(true)) }
                    }

                    override fun onFailure(call: Call<MessageDto>, t: Throwable) {
                        launch { send(Response.Error("Could not send Message :(", null)) }
                    }
                }
            )
            awaitClose { log("Message $message is send") }
        }
    }

    override suspend fun createUser(userName: String): Flow<Response<Boolean>> {
        val userBody = UserBody(
            name = userName,
            pkey = "",
            token = ""
        )
        return callbackFlow {
            service.createUser(postUserBody = userBody).enqueue(
                object : retrofit2.Callback<UserDto> {
                    override fun onResponse(
                        call: Call<UserDto>,
                        response: retrofit2.Response<UserDto>
                    ) {
                        launch { send(Response.Success(true)) }
                    }

                    override fun onFailure(call: Call<UserDto>, t: Throwable) {
                        launch { send(Response.Error("Could not create User :(", null)) }
                    }
                }
            )
            awaitClose { log("User $userName is created") }
        }

    }

    override suspend fun getMessage(messageId: Long): Response<Message> {

        return try {
            Response.Success(
                data = service.getMessage(messageId, "token").toMessage()
            )
        } catch (e: Exception) {
            log(e.message ?: "Could not get Message: $messageId")
            Response.Error("Could not get Message: $messageId")
        }
    }

    override suspend fun getQueue(userId: Long): Response<List<Queue>> {
        return try {
            Response.Success(
                data = service.getQueue(userId, "token").map { it.toQueue() }
            )
        } catch (e: Exception) {
            log(e.message ?: "Could not get Queue for: $userId")
            Response.Error("Could not get Queue for: $userId")
        }
    }


    override suspend fun getUser(userId: Long): Response<User> {
        return try {
            Response.Success(
                data = service.getUser(userId).toUser()
            )
        } catch (e: Exception) {
            log(e.message ?: "Could not get User: $userId")
            Response.Error("Could not get User: $userId")
        }
    }
}

