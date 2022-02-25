package com.sundayting.com.mine.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sundayting.com.common.ext.toast
import com.sundayting.com.common.widget.WaitDialogHelper
import com.sundayting.com.mine.UserViewModel
import com.sundayting.com.mine.databinding.FragmentLoginBinding
import com.sundayting.com.mine.register.RegisterFragment
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseBindingFragment<FragmentLoginBinding>() {

    @Inject
    lateinit var dialogHelper: WaitDialogHelper

    private val viewModel by activityViewModels<UserViewModel>()

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

        //监听加载框
        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .map { it.loading }
                .distinctUntilChanged()
                .collect { loading ->
                    if (loading) {
                        dialogHelper.showLoadingDialog("登陆中，请稍后")
                    } else {
                        dialogHelper.dismissDialog()
                    }
                }
        }

        //监听登陆成功
        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .mapNotNull { it.userBean }
                .collect {
                    findNavController().navigateUp()
                }
        }

        // TODO: 改用传统fragment传参？
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