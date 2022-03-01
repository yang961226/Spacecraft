package com.sundayting.com.mine

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kongzue.dialogx.dialogs.MessageDialog
import com.sundayting.com.common.UserViewModel
import com.sundayting.com.common.dao.WanDatabase
import com.sundayting.com.common.ext.toast
import com.sundayting.com.common.widget.NotificationHelper
import com.sundayting.com.mine.databinding.FragmentMineBinding
import com.sundayting.com.ui.BaseBindingFragment
import com.sundayting.com.ui.ext.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MineFragment : BaseBindingFragment<FragmentMineBinding>() {

    @Inject
    lateinit var wanDatabase: WanDatabase

    @Inject
    lateinit var notificationHelper: NotificationHelper
    private val userViewModel by activityViewModels<UserViewModel>()

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //回来的时候需要刷新主页文章
            userViewModel.changeHomeArticleUpdateTag(true)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectUiState()

        initView()

    }

    private fun initView() {
        binding.run {
            ivUserIcon.setOnClickListener {
                findNavController().navigate(MineFragmentDirections.actionMineFragmentToLoginFragment())
            }
            clCollect.setOnClickListener {
                lifecycleScope.launch {
                    if (wanDatabase.userDao().getUserLocal() == null) {
                        notificationHelper.showTip("请先登陆")
                    } else {
                        activityLauncher.launch(
                            Intent(
                                requireContext(),
                                CollectArticleActivity::class.java
                            )
                        )
                    }
                }
            }
            llLogout.setOnClickListener {
                MessageDialog.build()
                    .setTitle("退出登录")
                    .setMessage("退出后将无法收藏文章，查看用户信息等操作，确认退出吗？")
                    .setOkButton(
                        "确定"
                    ) { _, _ ->
                        userViewModel.logout()
                        false
                    }
                    .setCancelButton("取消") { _, _ ->
                        false
                    }
                    .show()
            }
        }
    }

    private fun collectUiState() {
        launchAndRepeatWithViewLifecycle {
            userViewModel.uiState
                .map { it.loading }
                .distinctUntilChanged()
                .collect { loading ->
                    if (loading) {
                        notificationHelper.showLoadingDialog("请稍后")
                    } else {
                        notificationHelper.dismissDialog()
                    }
                }
        }

        launchAndRepeatWithViewLifecycle {
            userViewModel.uiState
                .mapNotNull { it.message }
                .distinctUntilChanged()
                .collect { message ->
                    toast(message)
                    userViewModel.messageShown()
                }
        }

        launchAndRepeatWithViewLifecycle {
            userViewModel.uiState
                .map { it.userBean }
                .distinctUntilChanged()
                .collect { userBean ->
                    binding.run {
                        ivUserIcon.isEnabled = (userBean == null)
                        if (userBean != null) {
                            llLogout.visibility = View.VISIBLE

                            tvRanking.text = "${userBean.rank}"
                            tvUserName.text = userBean.username
                            tvIntegralNum.text = "${userBean.coinCount}"
                        } else {
                            llLogout.visibility = View.GONE

                            tvRanking.text = "0"
                            tvUserName.text = "点击头像登录/注册"
                            tvIntegralNum.text = "0"
                        }
                    }
                }
        }
    }

}