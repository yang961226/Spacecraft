package com.sundayting.com.mine.register

import androidx.lifecycle.viewModelScope
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.network.onFailure
import com.sundayting.com.network.onSuccess
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
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
        viewModelScope.launch {
            registerRepository.register(username, password, rePassword)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(registerSuccess = true)
                    }
                }
                .onFailure { failureReason ->
                    _uiState.update { uiState ->
                        uiState.copy(message = "$failureReason")
                    }
                }
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