package com.belkanoid.secretchat2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QueueDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("messageId")
    val messageId: Int,
    @SerializedName("timestamp")
    val timestamp: Int
)