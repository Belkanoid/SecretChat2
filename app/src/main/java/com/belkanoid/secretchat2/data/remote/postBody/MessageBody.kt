package com.belkanoid.secretchat2.data.remote.postBody

import com.google.gson.annotations.SerializedName

data class MessageBody(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("reciever") val receiver: Long,
    @SerializedName("sender") val sender: Long,
    @SerializedName("text") val message: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("edited") val isEdited: Boolean
)
