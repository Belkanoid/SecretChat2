package com.belkanoid.secretchat2.presentation.chatList.viewModel

sealed class ChatListEvent {
    class CreateNewUser(val userName: String): ChatListEvent()
    class SendNewMessage(val receiver: Long, val message: String): ChatListEvent()
}
