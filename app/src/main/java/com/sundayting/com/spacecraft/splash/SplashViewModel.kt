package com.sundayting.com.spacecraft.splash

import androidx.lifecycle.viewModelScope
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    //占位
) : BaseViewModel() {

    data class UiState(
        val directToNextActivity: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())

    val uiState = _uiState.immutable()

    fun hasPermission() {
        viewModelScope.launch {
            delay(1000L)
            _uiState.update {
                it.copy(directToNextActivity = true)
            }
        }
    }

}