package com.sundayting.com.spacecraft

import androidx.navigation.NavController
import com.sundayting.com.ui.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController

    override fun onNavigateUp() = navController.navigateUp()

}