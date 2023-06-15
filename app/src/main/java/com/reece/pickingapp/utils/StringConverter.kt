package com.reece.pickingapp.utils

import android.text.Html
import android.text.Spanned

object StringConverter {
    fun convertToHtmlSpan(string: String): Spanned {
        return Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
    }
}