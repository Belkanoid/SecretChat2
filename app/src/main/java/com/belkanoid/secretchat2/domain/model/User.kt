package com.belkanoid.secretchat2.domain.model

data class User(
    val id: Long,
    val name: String,
    val PublicKey:String,
    val token:String,
)
