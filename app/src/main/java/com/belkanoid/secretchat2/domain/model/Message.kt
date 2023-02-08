package com.belkanoid.secretchat2.domain.model


data class Message(
    val id: Long,
    val receiver: Long,
    val sender: Long,
    val message: String,
    val timestamp: Long,
    val isEdited: Boolean
)
