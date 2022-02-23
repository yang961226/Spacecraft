package com.sundayting.com.mine.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.sundayting.com.common.widget.toast
import com.sundayting.com.mine.LoginViewModel
import com.sundayting.com.mine.databinding.FragmentLoginBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class LoginFragment : BaseBindingFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            btnLogin.setOnClickListener {
                viewModel.login(
                    this.tietUsername.text.toString(),
                    this.tietPassword.text.toString()
                )
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .map { it.messages }
                .distinctUntilChanged()
                .collect { messageList ->
                    // TODO: 后面转用框架而不用toast
                    messageList.forEach {
                        toast(it.message)
                        viewModel.messageShown(it.id)
                    }
                }
        }

    }

}