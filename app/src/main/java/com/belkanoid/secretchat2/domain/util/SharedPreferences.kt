package com.belkanoid.secretchat2.domain.util

import android.content.Context
import javax.inject.Inject

class SharedPreferences @Inject constructor(
    private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(sharedName, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun putLong(name: String = USER_ID, value: Long) {
        editor.putLong(name, value)
        editor.apply()
    }

    fun getLong(name: String = USER_ID): Long {
        return sharedPreferences.getLong(name, -1)
    }

    fun putString(name: String, value: String) {
        val isExist = getString(name)
        if (isExist.isBlank()) {
            editor.putString(name, value)
            editor.apply()
        }
    }

    fun getString(name: String): String {
        return sharedPreferences.getString(name, "") ?: ""
    }

    companion object {
        private const val sharedName = "Shared Messenger"
        const val USER_ID = "userId"
        const val TOKEN = "token"
        const val PRIVATE_KEY = "privateKey"
        const val PUBLIC_KEY = "publicKey"
    }
}