package com.belkanoid.secretchat2.presentation.chatList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat2.domain.model.Message
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import com.belkanoid.secretchat2.domain.util.Response
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _chatListSate = MutableStateFlow<ChatListState>(ChatListState.Empty)
    val chatListState = _chatListSate.asStateFlow()


    private val currentUserId = sharedPreferences.getLong()
    init {
        _chatListSate.value = ChatListState.Loading
        val currentMessages = mutableListOf<Message>()
        viewModelScope.launch {
            while (true) {
                delay(800L)
                when(val response = repository.getMessages(currentMessages.map { it.id })) {
                    is Response.Success -> {
                        currentMessages.addAll(response.data ?: listOf())
                        _chatListSate.value = ChatListState.Data(currentMessages.sortedByDescending { it.timestamp })
                    }
                    is Response.Error -> {
                        _chatListSate.value = ChatListState.Error(response.message ?: "Unknown Error")
                    }
                }
            }
        }
    }

    fun onChatListAction(action: ChatListAction) {
        _chatListSate.value = ChatListState.Loading
        when(action) {
            is ChatListAction.OnCreateNewMessage -> {
                action.createNewMessage()
            }
            is ChatListAction.OnOpenMessage -> {
                action.openMessage()
            }
        }
    }
}
