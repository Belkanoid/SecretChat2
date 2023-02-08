package com.belkanoid.secretchat2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("id") val id: Long,
    @SerializedName("reciever") val receiver: Long,
    @SerializedName("sender") val sender: Long,
    @SerializedName("text") val message: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("edited") val isEdited: Boolean
)
