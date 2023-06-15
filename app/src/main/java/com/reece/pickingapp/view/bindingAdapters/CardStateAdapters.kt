package com.reece.pickingapp.view.bindingAdapters

import android.content.res.ColorStateList
import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.R
import com.reece.pickingapp.view.state.CardState
import com.reece.pickingapp.view.state.InputState

object CardStateAdapters {
    @BindingAdapter("cardUIForValidationState")
    @JvmStatic
    fun cardUIForValidationState(card: MaterialCardView, state: CardState?) {
        when (state) {
            is CardState.Error -> {
                card.strokeColor = card.context.getColor(R.color.morsco_red_1)
                card.strokeWidth =
                    card.context.resources.getInteger(R.integer.product_card_error_stroke_width)
            }
            else -> card.strokeWidth = 0
        }
    }

    @BindingAdapter("splitQuantityReadyForInputState")
    @JvmStatic
    fun splitQuantityReadyForInputState(view: View, isEnable: Boolean?) {
        isEnable?.let {
            when {
                isEnable -> {
                    view.apply {
                        alpha = 1.0F
                        isEnabled = true
                    }
                }
                else -> {
                    view.apply {
                        alpha = 0.5F
                        isEnabled = false
                    }
                }
            }
            }
    }
    @BindingAdapter("autoFillInputState")
    @JvmStatic
    fun autoFillInputState(materialButton: MaterialButton, isEnable: Boolean?) {
        isEnable?.let {
            when {
                isEnable -> {
                    val primaryColorState = ColorStateList.valueOf(materialButton.context.getColor(R.color.morsco_blue_5))
                    materialButton.apply {
                        setTextColor(materialButton.context.getColor(R.color.morsco_blue_5))
                        setStrokeColor(primaryColorState)
                    }
                    materialButton.apply {
                        alpha = 1.0F
                        isEnabled = true
                    }
                }
                else -> {
                    val primaryColorState = ColorStateList.valueOf(materialButton.context.getColor(R.color.morsco_grey_2))

                    materialButton.apply {
                        setTextColor(materialButton.context.getColor(R.color.morsco_grey_2))
                        setStrokeColor(primaryColorState)
                    }
                    materialButton.apply {
                        alpha = 0.5F
                        isEnabled = false
                    }
                }
            }
        }
    }
    @BindingAdapter("visibilityForState")
    @JvmStatic
    fun visibilityForState(view: View, state: CardState?) {
        when (state) {
            is CardState.Loading -> view.visibility = View.VISIBLE
            else -> view.visibility = View.INVISIBLE
        }
    }

    @BindingAdapter("isEnabledForState")
    @JvmStatic
    fun isEnabledForState(view: View, state: CardState?) {
        val enabled = when (state) {
            is CardState.Ready -> true
            is CardState.Error -> true
            else -> false
        }

        view.apply {
            isEnabled = enabled
        }
    }

    @BindingAdapter("takesFocus")
    @JvmStatic
    fun takesFocus(view: View, state: CardState?) {
        val takeFocus = when (state) {
            is CardState.Ready -> true
            else -> false
        }

        view.apply {
            isFocusableInTouchMode = takeFocus
            isFocusable = takeFocus
        }
        if (takeFocus) {
            view.requestFocus()
        } else {
            view.clearFocus()
        }
    }


    @BindingAdapter("inputValidationState")
    @JvmStatic
    fun inputValidationState(
        layout: TextInputLayout,
        state: InputState?,
    ) {
        var activeColor = layout.context.getColor(R.color.morsco_grey_1)
        var activeTextStyle = R.style.ProductDataEntryDefaultStyleText
        var activeHintTextStyle = R.style.ProductDataEntryHintStyleText
        var activeMessage: String? = null
        var activeAlpha = 1.0F
        layout.isEnabled = true
        layout.boxBackgroundColor =  layout.context.getColor(R.color.transparent_color)

        when (state) {
            is InputState.Success -> {
                activeColor = layout.context.getColor(R.color.morsco_green_1)
                activeTextStyle = R.style.ProductDataEntrySuccessStyleText
                activeMessage = state.message
            }
            is InputState.Error -> {
                activeColor = layout.context.getColor(R.color.morsco_red_2)
                activeTextStyle = R.style.ProductDataEntryErrorStyleText
                activeMessage = state.message
            }
            else -> {
                activeMessage = null
            }
        }

        val activeColorStateList = ColorStateList.valueOf(activeColor)

        layout.apply {
            error = activeMessage
            setErrorTextAppearance(activeTextStyle)
            setHintTextAppearance(activeHintTextStyle)
            setHelperTextTextAppearance(activeTextStyle)
            boxStrokeErrorColor = activeColorStateList
            boxStrokeColor = activeColor
            setErrorIconTintList(activeColorStateList)
            alpha = activeAlpha
        }
    }
}