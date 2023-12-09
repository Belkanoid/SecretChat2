package com.belkanoid.secretchat2.presentation.screens.registration.viewModel

sealed class RegistrationState {
    data class Success(val isRegistered: Boolean): RegistrationState()
    data class Error(val message: String): RegistrationState()
    object Loading: RegistrationState()
    object Empty: RegistrationState()

}
