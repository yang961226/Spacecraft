package com.sundayting.com.home.publish

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.widget.Tip
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.network.onFailure
import com.sundayting.com.network.onFinish
import com.sundayting.com.network.onSuccess
import com.sundayting.com.ui.BaseViewModel
import com.sundayting.com.ui.widget.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublishViewModel @Inject constructor(
    private val publishRepository: PublishRepository,
) : BaseViewModel() {

    private val _uiStateFlow = MutableStateFlow(UiState())

    val uiStateFlow = _uiStateFlow.immutable()

    data class UiState(
        val loadingState: LoadingState = LoadingState(),
        val publishSuccess: Boolean = false,
        val tipList: List<Tip> = arrayListOf()
    )

    fun publish(title: String, link: String) {
        if (title.isEmpty() || link.isEmpty()) {
            _uiStateFlow.update { uiState ->
                uiState.copy(
                    tipList = uiState.tipList + Tip("标题和链接不可为空")
                )
            }
            return
        }
        _uiStateFlow.update { uiState ->
            uiState.copy(
                loadingState = LoadingState(true, "发表文章中，，请稍后")
            )
        }
        viewModelScope.launch {
            publishRepository.publish(title, link)
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(
                                publishSuccess = true,
                                tipList = uiState.tipList + Tip("发表文章成功，审核后即可显示在文章页")
                            )
                        }
                    } else {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(
                                tipList = uiState.tipList + Tip("${it.responseBody.errorMsg}")
                            )
                        }
                    }
                }
                .onFailure {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip("$it")
                        )
                    }
                }
                .onFinish {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(
                            loadingState = LoadingState(loading = false)
                        )
                    }
                }
        }
    }

    fun tipShown(id: String) {
        _uiStateFlow.update { uiState ->
            uiState.copy(
                tipList = uiState.tipList.filterNot {
                    it.uuid == id
                }
            )
        }
    }

}