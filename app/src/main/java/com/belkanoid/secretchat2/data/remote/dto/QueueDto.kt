package com.belkanoid.secretchat2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QueueDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("userId")
    val userId: Long,
    @SerializedName("messageId")
    val messageId: Long,
    @SerializedName("timestamp")
    val timestamp: Long
)