package com.belkanoid.secretchat2.data.remote.postBody

import com.google.gson.annotations.SerializedName

data class UserBody(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String,
    @SerializedName("pkey") val pkey:String,
    @SerializedName("token") val token:String
)
