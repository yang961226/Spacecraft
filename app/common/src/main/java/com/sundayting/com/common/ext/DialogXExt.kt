package com.sundayting.com.common.ext

import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.OnBackPressedListener


fun DialogX.showPositiveDialog(message: String): WaitDialog {
    return TipDialog.show(message, WaitDialog.TYPE.SUCCESS)
}

fun DialogX.showNegativeDialog(message: String): WaitDialog {
    return TipDialog.show(message, WaitDialog.TYPE.ERROR)
}

fun DialogX.showLoadingDialog(
    message: String,
    cancelable: Boolean,
    onBackPressed: () -> Boolean
): WaitDialog {
    return WaitDialog
        .show(message).apply {
            isCancelable = cancelable
            onBackPressedListener = OnBackPressedListener(onBackPressed)
        }
}

fun DialogX.dismissWaitDialog() {
    WaitDialog.dismiss()
}