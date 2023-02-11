package com.belkanoid.secretchat2.presentation.chatList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat2.domain.model.Message
import com.belkanoid.secretchat2.domain.model.Queue
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import com.belkanoid.secretchat2.domain.util.Response
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import com.belkanoid.secretchat2.domain.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.resume

class ChatListViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _chatListSate = MutableStateFlow<ChatListState>(ChatListState.Empty)
    val chatListState = _chatListSate.asStateFlow()
    private val _currentMessagedId = MutableStateFlow<List<Long>>(listOf())

    var currentMessages: Flow<List<Message>> = flow {
        val listOfResponseMessages = repository.getMessages(_currentMessagedId.value)
        val messages = listOfResponseMessages.map { responseMessage ->
            when (responseMessage) {
                is Response.Success -> {
                    responseMessage.data!!
                }
                is Response.Error -> {
                    Message(-1L, -1L, -1L, responseMessage.message ?: "Error", 0, false)
                }
            }
        }
        _currentMessagedId.value = messages.map { it.id }
        emit(messages)
    }

    private val currentUserId by lazy {
        sharedPreferences.getLong()
    }


    fun sendMessage(receiver: Long, message: String): Flow<Boolean> = flow {
        val success: Boolean = repository.sendMessage(
            receiver = receiver,
            sender = currentUserId,
            message = message
        )
        emit(success)
    }

    fun createUser(userName: String): Boolean {
        return false
    }

    fun getUser() {

    }
}
