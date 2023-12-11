package com.belkanoid.secretchat2.presentation.screens.registration.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat2.domain.repository.NewUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val repository: NewUserRepository,
): ViewModel(){

    private val _registrationSate = MutableStateFlow<RegistrationState>(RegistrationState.Empty)
    val registrationSate = _registrationSate.asStateFlow()

    fun createUser(userName: String) {
        _registrationSate.value = RegistrationState.Loading
        if (!checkToValidName(userName)) {
            _registrationSate.value = RegistrationState.Error(message = "Invalid name")
            return
        }

        repository.createUser(userName)
            .onEach { success ->
                _registrationSate.value = RegistrationState.Success(success)
            }
            .launchIn(viewModelScope)
    }

    private fun checkToValidName(userName: String): Boolean {
        if (userName.contains("\n") || userName.contains("\r")) {
            _registrationSate.value = RegistrationState.Error("No allowed '\n' symbol")
            return false
        }

        if (userName.length < 3) {
            _registrationSate.value = RegistrationState.Error("No allowed name less than 3")
            return false
        }

        if (userName.length >= 10) {
            _registrationSate.value = RegistrationState.Error("No allowed name grater than 10")
            return false
        }

        return true
    }
}