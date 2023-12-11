package com.belkanoid.secretchat2.domain.repository

import kotlinx.coroutines.flow.Flow

interface NewUserRepository {

    fun createUser(userName: String): Flow<Boolean>

}