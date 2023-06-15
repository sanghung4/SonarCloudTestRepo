package com.reece.pickingapp.view.bindingAdapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import coil.load
import com.reece.pickingapp.R

object ImageAdapters {
    @BindingAdapter("imageToLoad", "placeholderDrawable")
    @JvmStatic
    fun imageToLoad(imageView: ImageView, urlString: String?, placeholderDrawable: Drawable?) {
        val defaultPlaceholder =
            AppCompatResources.getDrawable(imageView.context, R.drawable.image_placeholder)
        imageView.load(urlString) {
            crossfade(true)
            placeholder(placeholderDrawable ?: defaultPlaceholder)
            fallback(placeholderDrawable ?: defaultPlaceholder)
            error(placeholderDrawable ?: defaultPlaceholder)
        }
    }
}