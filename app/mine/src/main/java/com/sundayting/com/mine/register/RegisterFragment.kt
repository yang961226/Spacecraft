package com.sundayting.com.mine.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.sundayting.com.common.widget.WaitDialogHelper
import com.sundayting.com.common.widget.toast
import com.sundayting.com.mine.databinding.FragmentRegisterBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseBindingFragment<FragmentRegisterBinding>() {

    private val viewModel by viewModels<RegisterViewModel>()

    @Inject
    lateinit var waitDialogHelper: WaitDialogHelper

    companion object {
        const val REGISTER_SUCCESSFUL = "LOGIN_SUCCESSFUL"
    }

    private lateinit var previousSavedStateHandle: SavedStateHandle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousSavedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        previousSavedStateHandle.set(REGISTER_SUCCESSFUL, false)

        binding.run {
            btnRegister.setOnClickListener {
                viewModel.register(
                    etUsername.text.toString(),
                    etPassword.text.toString(),
                    etRePassword.text.toString()
                )
            }
            btnRegisterTest.setOnClickListener {
                viewModel.registerTest(
                    etUsername.text.toString(),
                    etPassword.text.toString(),
                    etRePassword.text.toString()
                )
            }
        }

        //监听通知
        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .mapNotNull {
                    it.message
                }.collect { message ->
                    toast(message)
                    viewModel.messageShown()
                }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .map { it.loading }
                .distinctUntilChanged()
                .collect { loading ->
                    if (loading) {
                        waitDialogHelper.showLoadingDialog("加载中，请稍后", cancelable = false)
                    } else {
                        waitDialogHelper.dismissDialog()
                    }
                }
        }

        //监听注册成功
        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .map { it.registerSuccess }
                .distinctUntilChanged()
                .collect { successful ->
                    if (successful) {
                        previousSavedStateHandle.set(REGISTER_SUCCESSFUL, true)
                        findNavController().popBackStack()
                    }
                }
        }
    }
}