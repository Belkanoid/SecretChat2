package com.belkanoid.secretchat2.domain.model

data class Queue(
    val id: Long,
    val userId: Long,
    val messageId: Long,
    val timestamp: Long
)
