package com.belkanoid.secretchat2.presentation.chatList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat2.domain.model.Message
import com.belkanoid.secretchat2.domain.model.Queue
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import com.belkanoid.secretchat2.domain.util.Response
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import com.belkanoid.secretchat2.domain.util.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

    private val _chatListSate = MutableStateFlow<ChatListState>(ChatListState.Empty)
    val chatListState = _chatListSate.asStateFlow()

    private val _messageList = MutableStateFlow<MutableList<Message>>(mutableListOf())

    private val currentUserId by lazy {
        sharedPreferences.getLong()
    }

    suspend fun getQueue() {

    }

    suspend fun sendMessage() {

    }

    suspend fun createUser(userName: String) {

    }

    private suspend fun getMessageList(queues: List<Queue>) {
    }

    private suspend fun getMessage(queue: Queue) {

    }

    suspend fun getUser() {

    }
}