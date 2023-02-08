package com.belkanoid.secretchat2.domain.model

import com.google.gson.annotations.SerializedName

data class Queue(
    val id: Int,
    val userId: Int,
    val messageId: Int,
    val timestamp: Int
)
