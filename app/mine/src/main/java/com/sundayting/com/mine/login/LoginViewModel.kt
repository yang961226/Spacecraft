package com.sundayting.com.mine.login

import com.sundayting.com.core.ext.immutable
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val message: String? = null
    )

    fun login(
        userName: String,
        password: String,
    ) {
        if (userName.isEmpty() || password.isEmpty()) {
            _uiState.update {
                it.copy(message = "请输入账号密码")
            }
        }
    }

    fun messageShown() {
        _uiState.update { state ->
            state.copy(message = null)
        }
    }

}