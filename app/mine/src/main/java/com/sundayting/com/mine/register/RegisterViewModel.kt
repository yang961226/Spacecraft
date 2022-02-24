package com.sundayting.com.mine.register

import androidx.lifecycle.viewModelScope
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.network.onFailure
import com.sundayting.com.network.onSuccess
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
        val registerSuccess: Boolean = false,
        val loading: Boolean = false,
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
            _uiState.update { uiState ->
                uiState.copy(loading = true)
            }
            registerRepository.register(username, password, rePassword)
                .onSuccess {
                    //网络调用成功，报文成功
                    if (it.responseBody.isSuccessful()) {
                        _uiState.update { state ->
                            state.copy(loading = false, registerSuccess = true)
                        }
                    }
                    //网络调用成功但是报文报错
                    else {
                        _uiState.update { state ->
                            state.copy(loading = false, message = "${it.responseBody.errorMsg}")
                        }
                    }

                }
                .onFailure { failureReason ->
                    _uiState.update { uiState ->
                        uiState.copy(loading = false, message = "$failureReason")
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
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(loading = true)
            }
            delay(2000L)
            _uiState.update { state ->
                state.copy(loading = false, registerSuccess = true)
            }
        }
    }

    fun messageShown() {
        _uiState.update { state ->
            state.copy(message = null)
        }
    }

}