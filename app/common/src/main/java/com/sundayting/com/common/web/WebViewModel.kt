package com.sundayting.com.common.web

import android.util.Log
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class WebViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val progressBarVisible: Boolean = true,
        val progress: Int = 0,
    )

    fun updateProgress(progress: Int) {
        Log.d("临时测试", "进度：$progress")
        if (progress < 0 || progress > 100) {
            throw IllegalAccessException("progress的值不能为$progress 请设置到0到100范围内")
        }
        _uiState.update { uiState ->
            uiState.copy(
                progress = progress,
                progressBarVisible = progress != 100
            )
        }
    }


}