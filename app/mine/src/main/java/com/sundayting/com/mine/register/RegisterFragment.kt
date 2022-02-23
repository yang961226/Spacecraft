package com.sundayting.com.mine.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.sundayting.com.common.widget.toast
import com.sundayting.com.mine.databinding.FragmentRegisterBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class RegisterFragment : BaseBindingFragment<FragmentRegisterBinding>() {

    private val viewModel by viewModels<RegisterViewModel>()

    companion object {
        const val REGISTER_SUCCESSFUL = "LOGIN_SUCCESSFUL"
        const val REGISTER_USERNAME = "REGISTER_USERNAME"
        const val REGISTER_PASSWORD = "REGISTER_PASSWORD"
    }

    private lateinit var previousSavedStateHandle: SavedStateHandle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        previousSavedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        previousSavedStateHandle.set(REGISTER_SUCCESSFUL, false)

        binding.run {
            btnRegister.setOnClickListener {

            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.uiState.run {
                mapNotNull {
                    it.message
                }.collect { message ->
                    toast(message)
                }

                map {
                    it.registerSuccess
                }.distinctUntilChanged()
                    .collect { successful ->
                        if (successful) {
                            previousSavedStateHandle.set(REGISTER_SUCCESSFUL, true)
                            previousSavedStateHandle.set(
                                REGISTER_USERNAME,
                                binding.etUsername.text.toString()
                            )
                            previousSavedStateHandle.set(
                                REGISTER_PASSWORD,
                                binding.etPassword.text.toString()
                            )
                        }
                    }
//                    .collect { userName->
//                        userName.let {
//                            previousSavedStateHandle.set(REGISTER_SUCCESSFUL,true)
//                            previousSavedStateHandle.set(REGISTER_USERNAME,this)
//                        }
//                        if(successful){
//
//                            findNavController().popBackStack()
//                        }
//                    }
            }
        }
    }

}