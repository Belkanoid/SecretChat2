package com.belkanoid.secretchat2.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat2.domain.repository.ChatRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val repository: ChatRepository,
): ViewModel(){

    private fun createUser(userName: String) {
        viewModelScope.launch {
            val isSuccess = repository.createUser(userName)
//            _chatListSate.value = ChatListStateMachine.ChatListState.OnCreateUser.IsUserCreated((isSuccess))
        }
    }
}