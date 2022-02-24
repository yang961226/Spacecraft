package com.sundayting.com.home

import androidx.fragment.app.viewModels
import com.sundayting.com.home.databinding.FragmentHomeBinding
import com.sundayting.com.ui.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

}