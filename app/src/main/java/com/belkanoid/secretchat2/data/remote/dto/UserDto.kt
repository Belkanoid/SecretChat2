package com.belkanoid.secretchat2.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("pkey")
    val PublicKey:String,
    @SerializedName("token")
    val token:String,
)
