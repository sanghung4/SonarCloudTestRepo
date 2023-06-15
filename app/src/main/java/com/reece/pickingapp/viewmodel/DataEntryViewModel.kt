package com.reece.pickingapp.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.utils.emptyString
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState


class DataEntryViewModel {

    //region LiveData Vars

    val inputState = MutableLiveData<InputState>()
    val inputFocusState = MutableLiveData<InputFocusState>()
    var entryString = MutableLiveData<String>()
    val hintString = MutableLiveData<String>()
    val inputCanBeCleared = MutableLiveData(true)
    val dataEntryFieldType = MutableLiveData(DataEntryFieldType.DEFAULT)
    val dataEntryEndIconMode = MutableLiveData(TextInputLayout.END_ICON_CUSTOM)

    //endregion

    private var textInputId: Int = 0

    init {
        setInputState(InputState.Default())
        setInputFocusState(InputFocusState.Default)
    }

    //region LiveData Updates

    /**
     * Updates the inputState
     *
     * Affected DataBinding in data_entry.xml
     *  - RelativeLayout -> layoutVisibility
     *      -- When .Hidden() sets entire input's visibility to GONE
     *
     *  - TextInputLayout -> inputValidationState
     *      -- Styles the Input Layout colors and behavior based on the state.
     *
     *  - TextInputEditText -> editState
     *      -- Sets whether the text field is focusable or editable
     *
     *  - ImageView -> imageVisibility
     *      -- Sets whether the delete icon is visible (tied to "inputCanBeCleared")
     */
    fun setInputState(state: InputState) {
        inputState.value = state
    }

    /**
     * Updates the inputFocusState.
     *
     * Affected DataBinding in data_entry.xml
     *  - TextInputLayout -> inputBoxForFocusState
     *      -- Styles the Input layout outline box color
     *  - TextInputEditText -> focusState
     *      -- Auto-focuses on a Edit Text field
     *      -- sets its ability to be focussed
     */
    fun setInputFocusState(state: InputFocusState) {
        inputFocusState.postValue(state)
    }

    /**
     * Sets the Hint string
     *
     * Affected DataBinding in data_entry.xml
     *  - TextInputLayout -> hint
     */
    fun setHintString(hint: String?) {
        hintString.value = hint ?: emptyString
    }

    /**
     * Sets dataEntryFieldType
     *
     * Affected DataBinding in data_entry.xml
     *  - TextInputLayout -> inputFieldType
     *      -- Tied to the inputValidationState() method
     *      -- sets the endIconDrawable and errorIconDrawable
     *  - TextInputEditText -> inputSettingsForViewModel
     *      -- used to set the keyListeners for allowed input characters
     *      -- used to inputType (ie: text or * for PASSWORD)
     */
    fun setDataEntryFieldType(type: DataEntryFieldType) {
        dataEntryFieldType.value = type
    }

    /**
     * Resets the TextInputEditText input string to empty string
     *
     * Affected DataBinding in data_entry.xml
     *  - TextInputEditText -> text
     */
    fun resetEntryString() {
        entryString.value = emptyString
    }

    /**
     * Sets whether the Input will get a delete icon and text can be reset
     *
     * Affected DataBinding in data_entry.xml
     *  - TextInputLayout -> inputCanBeCleared
     *      -- Tied to inputValidationState() method
     *      -- sets the drawable for the Input Layout
     *  - ImageView -> inputCanBeCleared
     *      -- Tied to imageVisibility() method
     *      -- Displays / Hides delete icon
     */
    fun setInputCanBeCleared(canClear: Boolean) {
        inputCanBeCleared.value = canClear
    }

    /**
     * Sets InputLayout's ability to have an icon
     *
     * Affected DataBinding in data_entry.xml
     *  - TextInputLayout -> endIconMode
     */
    fun setEndIconMode(iconMode: Int) {
        dataEntryEndIconMode.value = iconMode
    }

    //endregion

    //region Utility

    fun getInputStateValue(): InputState? {
        return inputState.value
    }

    fun getTextInputId(): Int {
        return textInputId
    }

    fun setTextInputId(id: Int) {
        textInputId = id
    }

    fun getInputString(): String? {
        return entryString.value
    }

    fun getFieldType(): DataEntryFieldType? {
        return dataEntryFieldType.value
    }

    fun entryIsBlank(): Boolean {
        return entryString.value == null || entryString.value.toString()
            .isBlank() || entryString.value.isNullOrBlank()
    }

    fun canBeValidated(): Boolean {
        return when (inputState.value) {
            is InputState.Hidden -> false
            is InputState.Disabled -> false
            else -> true
        }
    }

    //endregion

    //region Actions

    fun deleteButtonTapped() {
        resetEntryString()
        setInputState(InputState.Default())
        setInputFocusState(InputFocusState.Focussed)
    }
    //endregion
}