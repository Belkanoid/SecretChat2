package com.belkanoid.secretchat2.presentation.chatList.viewModel

import android.os.Message


sealed class ChatListState {
    class Data<T>(val data: List<T>): ChatListState()
    class Error(val message: String): ChatListState()
    object Loading: ChatListState()
    object Empty: ChatListState()
}