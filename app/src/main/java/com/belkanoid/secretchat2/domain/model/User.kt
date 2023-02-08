package com.belkanoid.secretchat2.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Long,
    val name: String,
    val PublicKey:String,
    val token:String,
)
