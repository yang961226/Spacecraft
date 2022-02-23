package com.sundayting.com.common.widget

import android.widget.Toast
import com.sundayting.com.ui.BaseActivity
import com.sundayting.com.ui.BaseFragment

fun BaseActivity.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun BaseFragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, duration).show()
}