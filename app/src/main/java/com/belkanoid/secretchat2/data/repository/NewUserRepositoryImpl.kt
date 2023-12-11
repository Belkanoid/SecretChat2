package com.belkanoid.secretchat2.data.repository

import com.belkanoid.secretchat2.data.remote.ChatApi
import com.belkanoid.secretchat2.data.remote.dto.UserDto
import com.belkanoid.secretchat2.data.remote.postBody.UserBody
import com.belkanoid.secretchat2.domain.repository.NewUserRepository
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class NewUserRepositoryImpl(
    private val service: ChatApi,
    private val sharedPreferences: SharedPreferences
): NewUserRepository {

    override suspend fun createUser(
        userName: String
    ): Flow<Boolean> = callbackFlow {
        val token = UUID.randomUUID().toString().replace("-", "")
        val publicKey = sharedPreferences.getString(SharedPreferences.PUBLIC_KEY)
        sharedPreferences.putString(SharedPreferences.TOKEN, token)
        val userBody = UserBody(
            name = userName,
            pkey = publicKey,
            token = token
        )

        service.createUser(postUserBody = userBody).enqueue(
            object : Callback<UserDto> {
                override fun onResponse(
                    call: Call<UserDto>, response: Response<UserDto>
                ) {
                    val userId = response.body()?.id ?: -1L
                    sharedPreferences.putLong(value = userId)
                    launch { this@callbackFlow.send(true) }
                }

                override fun onFailure(call: Call<UserDto>, t: Throwable) {
                    launch { this@callbackFlow.send(false) }
                }
            }
        )
    }
}