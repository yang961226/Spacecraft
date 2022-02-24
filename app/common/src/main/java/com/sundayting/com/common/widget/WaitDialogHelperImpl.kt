package com.sundayting.com.common.widget

import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.OnBackPressedListener
import javax.inject.Inject

class WaitDialogHelperImpl @Inject constructor() : WaitDialogHelper {
    override fun showPositiveDialog(message: String) {
        TipDialog.show(message, WaitDialog.TYPE.SUCCESS)
    }

    override fun showNegativeDialog(message: String) {
        TipDialog.show(message, WaitDialog.TYPE.ERROR)
    }

    override fun showLoadingDialog(
        message: String,
        cancelable: Boolean,
        onBackPressed: () -> Boolean
    ) {
        WaitDialog
            .show(message).apply {
                isCancelable = cancelable
                onBackPressedListener = OnBackPressedListener(onBackPressed)
            }
    }

    override fun dismissDialog() {
        WaitDialog.dismiss()
    }
}