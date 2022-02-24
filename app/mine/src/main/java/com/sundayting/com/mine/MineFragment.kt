package com.sundayting.com.mine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sundayting.com.common.widget.toast
import com.sundayting.com.mine.databinding.FragmentMineBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

@AndroidEntryPoint
class MineFragment : BaseBindingFragment<FragmentMineBinding>() {

    private val viewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .mapNotNull { it.message }
                .collect { message ->
                    toast(message)
                    viewModel.messageShown()
                }
        }



        launchAndRepeatWithViewLifecycle {
            viewModel.uiState
                .map { it.userBean }
                .distinctUntilChanged()
                .collect { userBean ->
                    binding.ivUserIcon.isEnabled = (userBean == null)
                    if (userBean != null) {
                        binding.run {
                            tvRanking.text = "${userBean.rank}"
                            tvUserName.text = userBean.username
                            tvIntegralNum.text = "${userBean.coinCount}"
                        }
                    } else {
                        // TODO: 恢复UI
                    }
                }
        }

        binding.ivUserIcon.setOnClickListener {
            findNavController().navigate(MineFragmentDirections.actionMineFragmentToLoginFragment())
        }
    }

}