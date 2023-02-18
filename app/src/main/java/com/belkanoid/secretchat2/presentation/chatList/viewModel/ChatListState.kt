package com.belkanoid.secretchat2.presentation.chatList.viewModel

import com.belkanoid.secretchat2.domain.model.Message


sealed class ChatListState {
    class Data(val data: List<Message>): ChatListState()
    class Error(val message: String): ChatListState()
    object Loading: ChatListState()
    object Empty: ChatListState()
}