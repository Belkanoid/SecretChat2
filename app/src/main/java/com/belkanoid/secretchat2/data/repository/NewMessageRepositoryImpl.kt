package com.belkanoid.secretchat2.data.repository

import com.belkanoid.secretchat2.data.mapper.toUser
import com.belkanoid.secretchat2.data.remote.ChatApi
import com.belkanoid.secretchat2.data.remote.dto.MessageDto
import com.belkanoid.secretchat2.data.remote.postBody.MessageBody
import com.belkanoid.secretchat2.data.rsa.RsaKey
import com.belkanoid.secretchat2.domain.model.User
import com.belkanoid.secretchat2.domain.repository.NewMessageRepository
import com.belkanoid.secretchat2.domain.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import java.util.Date
import javax.inject.Inject

class NewMessageRepositoryImpl @Inject constructor(
    private val service: ChatApi,
    private val rsaKey: RsaKey,
) : NewMessageRepository {

    override suspend fun sendMessage(
        receiver: Long,
        sender: Long,
        message: String
    ): Flow<Boolean> = callbackFlow {
        getUser(receiver)
            .map { response ->
                when (response) {
                    is Response.Success<User> -> response.data?.PublicKey
                    is Response.Error<User> -> throw RuntimeException(response.message)
                }
            }
            .catch { this@callbackFlow.send(false) }
            .filterNotNull()
            .map { key ->
                MessageBody(
                    receiver = receiver,
                    sender = sender,
                    message = rsaKey.encryptMessage(message, key),
                    timestamp = Date().time,
                    isEdited = false
                )
            }.collect {
                service.sendMessage(postMessageBody = it).enqueue(
                    object : Callback<MessageDto> {
                        override fun onResponse(
                            call: Call<MessageDto>, response: retrofit2.Response<MessageDto>
                        ) {
                            launch { this@callbackFlow.send(true) }
                        }

                        override fun onFailure(call: Call<MessageDto>, t: Throwable) {
                            launch { this@callbackFlow.send(false) }
                        }
                    }
                )
            }
        awaitClose {
            channel.close()
        }
    }

    override suspend fun getUser(userId: Long): Flow<Response<User>> = flow {
        try {
            val user = service.getUser(userId).toUser()
            emit(Response.Success(user))
        } catch (e: Exception) {
            emit(Response.Error("There is no user: $userId"))
        }
    }
}