@file:JvmName(name = "StringExtensions")

package com.reece.pickingapp.utils.extensions

import android.text.Html
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import com.google.android.material.textfield.TextInputEditText

fun String.toHtmlSpan(): Spanned {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
}

fun TextInputEditText.filterOnlyAlphaNumericCharacters() {
    filters = arrayOf(
        InputFilter { src, start, end, dst, dstart, dend ->
            src.filter { it.isLetterOrDigit() }
        }
    )
}

//  Leaves only numbers in string
fun String.filterToNum(): String {
    val regex = Regex("[^0-9]")
    return regex.replace(this, "")
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}