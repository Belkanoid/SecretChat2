package com.belkanoid.secretchat2.presentation.chatList.viewModel

import com.belkanoid.secretchat2.domain.model.Message

sealed class ChatListState {
    sealed class Success(val data: List<Message>): ChatListState()
    class Error(val message: String): ChatListState()
    object Loading: ChatListState()
    class OnSendMessage(val isSuccess: Boolean): ChatListState()
    sealed class OnCreateUser: ChatListState() {
        object NeedToCreateUser: OnCreateUser()
        class IsUserCreated(val success: Boolean): OnCreateUser()
    }
    object Empty: ChatListState()
}
