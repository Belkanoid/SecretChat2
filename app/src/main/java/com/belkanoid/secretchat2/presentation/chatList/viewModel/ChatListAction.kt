package com.belkanoid.secretchat2.presentation.chatList.viewModel

sealed class ChatListAction {
    data class OnCreateNewMessage(val createNewMessage:() -> Unit): ChatListAction()
    data class OnOpenMessage(val openMessage: () -> Unit): ChatListAction()
}
