package com.reece.pickingapp.view.bindingAdapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.R
import com.reece.pickingapp.interfaces.BarCodeScanInterface
import com.reece.pickingapp.interfaces.DataEntryInterface
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.utils.extensions.filterOnlyAlphaNumericCharacters
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import com.reece.pickingapp.viewmodel.DataEntryViewModel


/**
 * See DataEntryViewModel for usage of the BindingAdapter methods
 */
object DataEntryAdapters {

    @BindingAdapter("layoutVisibility")
    @JvmStatic
    fun layoutVisibility(layout: RelativeLayout, state: InputState?) {
        val activeVisibility = when (state) {
            is InputState.Hidden -> View.GONE
            else -> View.VISIBLE
        }

        layout.apply {
            visibility = activeVisibility
        }
    }

    @BindingAdapter("imageVisibility", "inputCanBeCleared")
    @JvmStatic
    fun imageVisibility(image: ImageView, state: InputState?, inputCanBeCleared: Boolean?) {

        var activeVisibility = when (state) {
            is InputState.Success -> View.VISIBLE
            else -> View.GONE
        }

        inputCanBeCleared?.let {
            if (!inputCanBeCleared) {
                activeVisibility = View.GONE
            }
        }

        image.apply {
            visibility = activeVisibility
        }
    }

    @BindingAdapter("onClickEndIcon", "inputId")
    @JvmStatic
    fun onClickEndIcon(
        layout: TextInputLayout,
        barCodeScanInterface: BarCodeScanInterface?,
        inputId: Int?) {

        layout.setEndIconOnClickListener {
            barCodeScanInterface?.onClickScan(inputId ?: 0, layout.hint.toString())
        }
    }

    @BindingAdapter("inputValidationState", "inputFieldType", "inputCanBeCleared")
    @JvmStatic
    fun inputValidationState(
        layout: TextInputLayout,
        state: InputState?,
        inputFieldType: DataEntryFieldType?,
        inputCanBeCleared: Boolean?
    ) {
        var activeColor = layout.context.getColor(R.color.morsco_grey_1)
        var activeTextStyle = R.style.ProductDataEntryDefaultStyleText
        var activeHintTextStyle = R.style.ProductDataEntryHintStyleText
        var activeMessage: String? = null
        var activeDrawable: Drawable? =
            activeDrawableForFieldType(type = inputFieldType, layout.context)
        var activeAlpha = 1.0F
        layout.isEnabled = true
        layout.boxBackgroundColor =  layout.context.getColor(R.color.transparent_color)

        when (state) {
            is InputState.Success -> {
                activeColor = layout.context.getColor(R.color.morsco_green_1)
                activeTextStyle = R.style.ProductDataEntrySuccessStyleText
                activeMessage = state.message
                inputCanBeCleared?.let {
                    if (inputCanBeCleared) {
                        activeDrawable = null
                        layout.isEndIconVisible = false
                    }
                }
            }
            is InputState.Error -> {
                activeColor = layout.context.getColor(R.color.morsco_red_2)
                activeTextStyle = R.style.ProductDataEntryErrorStyleText
                activeMessage = state.message
                layout.isEndIconVisible = true
            }
            is InputState.Disabled -> {
                if (inputFieldType == DataEntryFieldType.SCANNABLE_SERIAL || inputFieldType == DataEntryFieldType.QUANTITY_NUMBER){
                    activeColor = layout.context.getColor(R.color.morsco_grey_1)
                    activeTextStyle = R.style.ProductDataEntryDisableStyleText
                    layout.isEnabled = false
                    layout.boxBackgroundColor =  layout.context.getColor(R.color.morsco_disable_color)
                }else {
                    activeAlpha = 0.5F
                    layout.isEnabled = false
                }
            }
            else -> {
                activeMessage = null
                layout.isEndIconVisible = activeDrawable != null
            }
        }

        val activeColorStateList = ColorStateList.valueOf(activeColor)

        layout.apply {
            error = activeMessage
            setErrorTextAppearance(activeTextStyle)
            setHintTextAppearance(activeHintTextStyle)
            setHelperTextTextAppearance(activeTextStyle)
            endIconDrawable = activeDrawable
            boxStrokeErrorColor = activeColorStateList
            boxStrokeColor = activeColor
            if (inputFieldType == DataEntryFieldType.SCANNABLE_SERIAL || inputFieldType ==  DataEntryFieldType.SCANNABLE_PRODUCT){
                errorIconDrawable = null
            }
            setErrorIconTintList(activeColorStateList)
            alpha = activeAlpha
        }
    }

    @BindingAdapter("inputBoxForFocusState", "dataEntryInterface")
    @JvmStatic
    fun inputBoxForFocusState(
        layout: TextInputLayout,
        state: InputFocusState?,
        inputInterface: DataEntryInterface?
    ) {
        var activeColor = layout.context.getColor(R.color.morsco_grey_1)


        when (state) {
            is InputFocusState.Focussed -> {
                activeColor =
                    layout.context.getColor(R.color.morsco_blue_5)
                inputInterface?.onInputComponentFocus(component = (layout.parent) as ViewGroup)
            }
            else -> {
                val defaultStrokeColorField =
                    TextInputLayout::class.java.getDeclaredField("defaultStrokeColor")
                defaultStrokeColorField.isAccessible = true
                defaultStrokeColorField.set(layout, activeColor)
            }
        }

        layout.boxStrokeColor = activeColor
    }

    @BindingAdapter("focusState")
    @JvmStatic
    fun focusState(editText: TextInputEditText, state: InputFocusState?) {

        when (state) {
            is InputFocusState.Focussed -> {
                editText.apply {
                    isFocusableInTouchMode = true
                    isFocusable = true
                    isClickable = true
                    isCursorVisible = true
                    isEnabled = true
                }
                editText.requestFocus()
                editText.setSelection(editText.length())
            }
            is InputFocusState.NonFocusable -> {
                editText.clearFocus()
                editText.apply {
                    isFocusableInTouchMode = false
                    isFocusable = false
                    isClickable = false
                    isCursorVisible = false
                    isEnabled = false
                }
            }
            is InputFocusState.ClearFocus -> {
                editText.clearFocus()
                editText.apply {
                    isFocusableInTouchMode = true
                    isFocusable = true
                    isClickable = true
                    isEnabled = true
                }
            }
            else -> {
                editText.apply {
                    isFocusableInTouchMode = true
                    isFocusable = true
                    isClickable = true
                    isEnabled = true
                }
            }
        }
    }

    @BindingAdapter("editState")
    @JvmStatic
    fun editState(editText: TextInputEditText, state: InputState?) {
        val focusable: Boolean
        val enabled: Boolean

        when (state) {
            is InputState.Success -> {
                focusable = true
                enabled = true
            }
            is InputState.Error -> {
                focusable = true
                enabled = true
            }
            is InputState.Default -> {
                focusable = true
                enabled = true
            }
            else -> {
                focusable = false
                enabled = false
            }
        }

        editText.apply {
            isFocusable = focusable
            isFocusableInTouchMode = focusable
            isLongClickable = focusable
            isCursorVisible = focusable
            isEnabled = enabled
        }
    }

    @BindingAdapter("inputSettingsForViewModel")
    @JvmStatic
    fun inputSettingsForViewModel(
        textInputEditText: TextInputEditText,
        viewModel: DataEntryViewModel?
    ) {

        textInputEditText.setSingleLine()

        viewModel?.getFieldType()?.let { type ->
            when (type) {
                DataEntryFieldType.SCANNABLE_PRODUCT -> {
                    textInputEditText.apply {
                        inputType =
                            InputType.TYPE_CLASS_NUMBER
                    }
                }
                DataEntryFieldType.SCANNABLE_SERIAL -> {
                    textInputEditText.apply {
                        filterOnlyAlphaNumericCharacters()
                    }
                }
                DataEntryFieldType.PASSWORD -> {
                    textInputEditText.apply {
                        inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                }
                DataEntryFieldType.NUMBER_ENTRY -> {
                    textInputEditText.apply {
                        inputType = InputType.TYPE_CLASS_NUMBER
                    }
                }
                DataEntryFieldType.QUANTITY_NUMBER -> {
                    textInputEditText.apply {
                        inputType = InputType.TYPE_CLASS_NUMBER
                    }
                }
                else -> {
                    textInputEditText.apply {
                        inputType =
                            InputType.TYPE_CLASS_TEXT
                    }
                }
            }
        }

        textInputEditText.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus && viewModel?.inputFocusState?.value != InputFocusState.Focussed) {
                    viewModel?.setInputFocusState(InputFocusState.Focussed)
                }
            }

        viewModel?.setTextInputId(textInputEditText.hashCode())
    }

    private fun activeDrawableForFieldType(type: DataEntryFieldType?, context: Context): Drawable? {
        return when (type) {
            DataEntryFieldType.SCANNABLE_PRODUCT -> AppCompatResources.getDrawable(
                context,
                R.drawable.ic_barcode
            )
            DataEntryFieldType.SCANNABLE_SERIAL -> AppCompatResources.getDrawable(
                context,
                R.drawable.ic_barcode
            )
            else -> null
        }
    }
}