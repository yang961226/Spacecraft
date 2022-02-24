package com.sundayting.com.mine.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sundayting.com.common.widget.toast
import com.sundayting.com.mine.databinding.FragmentLoginBinding
import com.sundayting.com.mine.register.RegisterFragment
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull

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
            tvRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }

        //监听通知
        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .mapNotNull { it.message }
                .collect {
                    toast(it)
                    viewModel.messageShown()
                }
        }

        // TODO: 改用传统fragment传参？测试github提交，无意义
        findNavController().currentBackStackEntry!!.let { entry ->
            entry.savedStateHandle.getLiveData<Boolean>(RegisterFragment.REGISTER_SUCCESSFUL)
                .observe(entry) {
                    if (it) {
                        toast("注册成功，请登录")
                        //清空残留数据，避免倒灌
                        entry.savedStateHandle.set(RegisterFragment.REGISTER_SUCCESSFUL, false)
                    }
                }
        }
    }

}