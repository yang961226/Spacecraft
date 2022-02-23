package com.sundayting.com.mine

import com.sundayting.com.common.widget.InstanceMessage
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MineUiState())
    val uiState = _uiState.immutable()

    data class MineUiState(
        val messages: List<InstanceMessage> = emptyList()
    )

    fun login(
        userName: String,
        password: String,
    ) {
        _uiState.update {
            it.copy(messages = listOf(InstanceMessage("功能还没做，别点我")))
        }
    }

    fun messageShown(id: String) {
        _uiState.update { state ->
            state.copy(messages = state.messages.filter { message ->
                message.id != id
            })
        }
    }

}