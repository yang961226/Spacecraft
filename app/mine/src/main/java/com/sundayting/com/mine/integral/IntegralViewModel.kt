package com.sundayting.com.mine.integral

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.bean.IntegralBean
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
class IntegralViewModel @Inject constructor(
    private val integralRepository: IntegralRepository
) : BaseViewModel() {

    private val _uiStateFlow = MutableStateFlow((UiState()))
    val uiStateFlow = _uiStateFlow.immutable()

    data class UiState(
        val swipeRefreshing: Boolean = false,
        val tipList: List<Tip> = emptyList(),
        val integralBeanList: List<IntegralBean> = emptyList(),
    )

    init {
        clearAndRefreshIntegralRecord()
    }

    fun clearAndRefreshIntegralRecord() {
        getIntegralRecord(0, true)
    }

    fun tipShown(id: String) {
        _uiStateFlow.update { uiState ->
            uiState.copy(
                tipList = uiState.tipList.filterNot {
                    id == it.uuid
                }
            )
        }
    }

    private fun getIntegralRecord(page: Long, clear: Boolean) {
        _uiStateFlow.update { uiState ->
            uiState.copy(
                swipeRefreshing = clear
            )
        }
        viewModelScope.launch {
            integralRepository.getIntegralRecord(page)
                .onSuccess {
                    if (it.responseBody.isSuccessful()) {
                        it.responseBody.data?.datas?.let { integralBeanList ->
                            _uiStateFlow.update { uiState ->
                                uiState.copy(
                                    integralBeanList = (if (clear) emptyList() else uiState.integralBeanList) + integralBeanList
                                )
                            }
                        }
                    } else {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(
                                tipList = uiState.tipList + Tip("请求失败")
                            )
                        }
                    }
                }
                .onFailure {
                    _uiStateFlow.update { uiState ->
                        uiState.copy(
                            tipList = uiState.tipList + Tip(it.orEmpty())
                        )
                    }
                }
                .onFinish {
                    if (clear) {
                        _uiStateFlow.update { uiState ->
                            uiState.copy(
                                swipeRefreshing = false
                            )
                        }
                    }
                }
        }
    }

}