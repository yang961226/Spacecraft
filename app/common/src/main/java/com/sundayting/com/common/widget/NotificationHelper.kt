package com.sundayting.com.common.widget

interface NotificationHelper {

    fun showTip(tip: String)

    fun showPositiveDialog(message: String)

    fun showNegativeDialog(message: String)

    fun showLoadingDialog(
        message: String,
        cancelable: Boolean = false,
        onBackPressed: (() -> Boolean) = { false }
    )

    fun dismissDialog()

}