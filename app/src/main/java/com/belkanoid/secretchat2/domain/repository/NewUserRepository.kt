package com.belkanoid.secretchat2.domain.repository

import kotlinx.coroutines.flow.Flow

interface NewUserRepository {

    suspend fun createUser(userName: String): Flow<Boolean>

}