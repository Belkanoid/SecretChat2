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


    val currentUserId: Long
        get() = sharedPreferences.getLong()
    init {
        _chatListSate.value = ChatListState.Loading
        if(currentUserId == -1L) {
            _chatListSate.value = ChatListState.OnCreateUser.NeedToCreateUser
        }
    }

    var currentMessages: Flow<List<Message>> = flow {
        val currentMessages = mutableListOf<Message>()
        var loopCount = 0
        while (true) {
            delay(500L)
            log("ChatListViewModel", loopCount++.toString())
            val listOfResponseMessages = repository.getMessages(currentMessages.map { it.id })
            val messages = listOfResponseMessages.map { responseMessage ->
                when (responseMessage) {
                    is Response.Success -> {
                        responseMessage.data!!
                    }
                    is Response.Error -> {
                        Message(-1L, -1L, currentUserId, responseMessage.message ?: "Error", 0, false)
                    }
                }
            }
            currentMessages.addAll(messages)
            emit(currentMessages)
        }
    }

    fun onChatListEvent(event: ChatListEvent) {
        _chatListSate.value = ChatListState.Loading
        when(event) {
            is ChatListEvent.SendNewMessage -> {
                sendMessage(event.receiver, event.message)
            }
            is ChatListEvent.CreateNewUser -> {
                createUser(event.userName)
            }
        }
    }

    private fun sendMessage(receiver: Long, message: String) {
        viewModelScope.launch {
            val isSuccess = repository.sendMessage(receiver, currentUserId, message)
            _chatListSate.value = ChatListState.OnSendMessage(isSuccess)
        }
    }

    private fun createUser(userName: String) {
        viewModelScope.launch {
            val isSuccess = repository.createUser(userName)
            _chatListSate.value = ChatListState.OnCreateUser.IsUserCreated((isSuccess))
        }
    }

}
