package com.sundayting.com.mine

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.bean.UserBean
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
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val loading: Boolean = false,
        val message: String? = null,
        val userBean: UserBean? = null
    )

    init {
        viewModelScope.launch {
            userRepository.getLocalUserBean()?.let { userBeanCached ->
                _uiState.update { uiState ->
                    uiState.copy(userBean = userBeanCached)
                }
            }
        }
    }

    fun login(
        username: String,
        password: String,
    ) {
        if (username.isEmpty() || password.isEmpty()) {
            _uiState.update {
                it.copy(message = "请输入账号密码")
            }
            return
        }
        _uiState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            userRepository.login(username, password)
                .onSuccess { apiResponse ->
                    if (apiResponse.responseBody.isSuccessful()) {
                        apiResponse.responseBody.data?.let { userBean ->
                            userRepository.cacheUserBean(userBean)
                            _uiState.update { uiState ->
                                uiState.copy(message = "登陆成功", userBean = userBean)
                            }
                        }
                    } else {
                        _uiState.update { uiState ->
                            uiState.copy(message = apiResponse.responseBody.errorMsg)
                        }
                    }
                }
                .onFailure { failureReason ->
                    _uiState.update { uiState ->
                        uiState.copy(message = failureReason)
                    }
                }
            _uiState.update {
                it.copy(loading = false)
            }
        }

    }

    fun messageShown() {
        _uiState.update { state ->
            state.copy(message = null)
        }
    }

}