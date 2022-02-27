package com.sundayting.com.common

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.bean.UserBean
import com.sundayting.com.common.dao.WanDatabase
import com.sundayting.com.common.widget.Tip
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.network.onFailure
import com.sundayting.com.network.onFinish
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
    private val wanDatabase: WanDatabase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val loading: Boolean = false,
        val message: String? = null,
        val userBean: UserBean? = null,
        val tipList: List<Tip> = listOf(),
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

    fun logout() {
        _uiState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            if (userRepository.getLocalUserBean() == null) {
                return@launch
            }
            userRepository.logout()
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
                        _uiState.update { uiState ->
                            uiState.copy(
                                userBean = null
                            )
                        }
                        wanDatabase.userDao().clearUser()
                    } else {
                        _uiState.update { uiState ->
                            uiState.copy(tipList = uiState.tipList + Tip(it.responseBody.errorMsg.orEmpty()))
                        }
                    }
                }.onFailure {
                    _uiState.update { uiState ->
                        uiState.copy(tipList = uiState.tipList + Tip(it.orEmpty()))
                    }
                }
                .onFinish {
                    _uiState.update {
                        it.copy(loading = false)
                    }
                }
        }
    }

    fun tipShown(tipId: String) {
        _uiState.update { uiState ->
            uiState.copy(tipList = uiState.tipList.filterNot { it.uuid == tipId })
        }
    }

    fun messageShown() {
        _uiState.update { state ->
            state.copy(message = null)
        }
    }

}