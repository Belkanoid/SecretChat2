package com.belkanoid.secretchat2.presentation.chatList.viewModel

import androidx.lifecycle.ViewModel
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

    private val _chatListSate = MutableStateFlow(ChatListState.Empty)
    val chatListState = _chatListSate.asStateFlow()

    suspend fun getQueue() {

    }

    suspend fun sendMessage() {

    }

    suspend fun createUser(userName: String) {

    }

    suspend fun getMessage() {

    }

    suspend fun getUser() {

    }
}