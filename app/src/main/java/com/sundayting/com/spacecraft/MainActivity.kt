package com.sundayting.com.spacecraft

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sundayting.com.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment).navController.let {
            navController = it
            findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(it)
            it.addOnDestinationChangedListener { _, _, arguments ->
                // TODO: 暂留
            }
        }
    }

    override fun onNavigateUp() = navController.navigateUp()

}