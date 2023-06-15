package com.reece.pickingapp.wrappers

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialAlertDialogBuilderWrapper {
    fun create(context: Context, overrideThemeResId: Int): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context, overrideThemeResId)
    }
}