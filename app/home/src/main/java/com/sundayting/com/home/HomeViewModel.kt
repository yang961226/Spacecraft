package com.sundayting.com.home

import androidx.lifecycle.viewModelScope
import com.sundayting.com.common.bean.BannerBean
import com.sundayting.com.core.ext.immutable
import com.sundayting.com.home.banner.BannerRepository
import com.sundayting.com.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bannerRepository: BannerRepository
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.immutable()

    data class UiState(
        val banner: MutableList<BannerBean> = arrayListOf()
    )

    init {
        getBanner()
    }

    private fun getBanner() {
        viewModelScope.launch {
            _uiState.update { uiState ->
                bannerRepository.getBanner().let {
                    if (it == null) {
                        uiState.copy(banner = arrayListOf())
                    } else {
                        uiState.copy(banner = it)
                    }
                }
            }
        }
    }

}