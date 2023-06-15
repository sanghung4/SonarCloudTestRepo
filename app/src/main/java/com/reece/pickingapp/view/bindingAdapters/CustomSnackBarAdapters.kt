package com.reece.pickingapp.view.bindingAdapters

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.card.MaterialCardView
import com.reece.pickingapp.R
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.view.state.SnackBarState

object CustomSnackBarAdapters {
    @BindingAdapter("snackBarCardViewAppearance")
    @JvmStatic
    fun snackBarCardViewAppearance(cardView: MaterialCardView, state: SnackBarState?) {
        state?.let {
            val primaryColor = cardView.context.getColor(primaryColorByState(state))
            val primaryColorState = ColorStateList.valueOf(primaryColor)
            val secondaryColor = cardView.context.getColor(secondaryColorByState(state))
            val secondaryColorState = ColorStateList.valueOf(secondaryColor)
            cardView.apply {
                setCardBackgroundColor(secondaryColorState)
                setStrokeColor(primaryColorState)
            }
        }
    }

    @BindingAdapter("snackBarIconForState")
    @JvmStatic
    fun snackBarIconForState(imageView: ImageView, state: SnackBarState?) {
        state?.let {
            val iconDrawableRef = when (state.type) {
                SnackBarType.SUCCESS -> R.drawable.ic_circle_check
                SnackBarType.ERROR -> R.drawable.ic_circle_alert
                SnackBarType.MESSAGE -> R.drawable.ic_circle_info
                SnackBarType.WARNING -> R.drawable.ic_circle_alert
            }

            imageView.load(iconDrawableRef)
        }
    }

    @BindingAdapter("imageViewColorByState")
    @JvmStatic
    fun imageViewColorByState(imageView: ImageView, state: SnackBarState?) {
        state?.let {
            val activeColor = imageView.context.getColor(primaryColorByState(state))
            imageView.imageTintList = ColorStateList.valueOf(activeColor)
        }
    }

    @BindingAdapter("textViewColorByState")
    @JvmStatic
    fun textViewColorByState(textView: TextView, state: SnackBarState?) {
        state?.let {
            textView.setTextColor(textView.context.getColor(primaryColorByState(state)))
        }
    }

    private fun primaryColorByState(state: SnackBarState): Int {
        return when (state.type) {
            SnackBarType.SUCCESS -> R.color.morsco_green_1
            SnackBarType.ERROR -> R.color.morsco_red_2
            SnackBarType.MESSAGE -> R.color.morsco_blue_2
            SnackBarType.WARNING -> R.color.morsco_brown_1
        }
    }

    private fun secondaryColorByState(state: SnackBarState): Int {
        return when (state.type) {
            SnackBarType.SUCCESS -> R.color.morsco_green_2
            SnackBarType.ERROR -> R.color.morsco_red_3
            SnackBarType.MESSAGE -> R.color.morsco_blue_3
            SnackBarType.WARNING -> R.color.morsco_brown_2
        }
    }
}