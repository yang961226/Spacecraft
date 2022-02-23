package com.sundayting.com.mine

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.sundayting.com.mine.databinding.FragmentMineBinding
import com.sundayting.com.ui.BaseBindingFragment

class MineFragment : BaseBindingFragment<FragmentMineBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivUserIcon.setOnClickListener {
            findNavController().navigate(MineFragmentDirections.actionMineFragmentToLoginFragment())
        }
    }

}