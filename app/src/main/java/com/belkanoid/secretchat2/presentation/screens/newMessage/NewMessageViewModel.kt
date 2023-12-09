package com.belkanoid.secretchat2.presentation.screens.newMessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat2.domain.repository.NewMessageRepository
import com.belkanoid.secretchat2.domain.util.SharedPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewMessageViewModel @Inject constructor(
    private val repository: NewMessageRepository,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

    private val currentUserId = sharedPreferences.getLong()

    private fun sendMessage(receiver: Long, message: String) {
        viewModelScope.launch {
            val isSuccess = repository.sendMessage(receiver, currentUserId, message)
//            _chatListSate.value = ChatListStateMachine.ChatListState.OnSendMessage(isSuccess)
        }
    }
}