package com.belkanoid.secretchat2.domain.model

import com.google.gson.annotations.SerializedName

data class Queue(
    val id: Long,
    val userId: Long,
    val messageId: Long,
    val timestamp: Long
)
