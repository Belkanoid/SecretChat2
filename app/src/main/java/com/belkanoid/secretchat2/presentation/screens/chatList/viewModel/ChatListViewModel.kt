package com.belkanoid.secretchat2.presentation.screens.chatList.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat2.domain.repository.ChatListRepository
import com.belkanoid.secretchat2.domain.repository.NewMessageRepository
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val repository: ChatListRepository,
    private val dva: NewMessageRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val refreshFlow = MutableSharedFlow<ChatListState>()
    private val currentMessagesId = mutableListOf<Long>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = repository.messages
        .mapLatest {messages->
            currentMessagesId.addAll(messages.map { it.id })
            ChatListState.Data(data = messages) as ChatListState
        }
        .catch { ChatListState.Error(message = it.message ?: "Неизвестная ошибка") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ChatListState.Empty
        )

    val id = sharedPreferences.getLong()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                repository.refreshMessageList(currentMessagesId)
            }
        }
    }

    fun createNewMessage() {
        viewModelScope.launch() {
            refreshFlow.emit(ChatListState.Loading)
        }
    }

    fun openMessage() {
        viewModelScope.launch() {
            refreshFlow.emit(ChatListState.Loading)
        }
    }
}
