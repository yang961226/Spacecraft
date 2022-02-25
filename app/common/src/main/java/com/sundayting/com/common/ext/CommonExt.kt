package com.sundayting.com.common.ext

import android.widget.Toast
import com.sundayting.com.ui.BaseActivity
import com.sundayting.com.ui.BaseFragment

fun BaseActivity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun BaseFragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}