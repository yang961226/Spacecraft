package com.sundayting.com.mine.register

import com.sundayting.com.core.ext.immutable
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val message: String? = null,
        val registerSuccess: Boolean = false
    )

    fun register(username: String, password: String, rePassword: String) {
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            _uiState.update { uiState ->
                uiState.copy(message = "请输入账号和密码")
            }
            return
        }
        if (password != rePassword) {
            _uiState.update { uiState ->
                uiState.copy(message = "两次输入的密码不一致，请确认")
            }
            return
        }
        _uiState.update { uiState ->
            uiState.copy(message = "功能未制作")
        }
    }

    fun registerTest(username: String, password: String, rePassword: String) {
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
            _uiState.update { uiState ->
                uiState.copy(message = "请输入账号和密码")
            }
            return
        }
        if (password != rePassword) {
            _uiState.update { uiState ->
                uiState.copy(message = "两次输入的密码不一致，请确认")
            }
            return
        }
        _uiState.update { state ->
            state.copy(registerSuccess = true)
        }
    }

    fun messageShown() {
        _uiState.update { state ->
            state.copy(message = null)
        }
    }

}