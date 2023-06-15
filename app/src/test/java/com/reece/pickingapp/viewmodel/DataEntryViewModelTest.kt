package com.reece.pickingapp.viewmodel

import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DataEntryViewModelTest : DescribeSpec({
    describe("DataEntryViewModel") {
        lateinit var uut: DataEntryViewModel

        beforeEach {
            uut = DataEntryViewModel()
        }

        describe("when init{}") {
            describe("when .setInputState()") {
                it("sets .inputState.value to InputState.Default()") {
                    uut.inputState.value!!::class shouldBe InputState.Default::class
                }
            }
            describe("when .setInputFocusState()") {
                it("sets .inputFocusState.value to InputFocusState.Default") {
                    uut.inputFocusState.value!!::class shouldBe InputFocusState.Default::class
                }
            }
        }

        describe("when .setInputState()") {
            beforeEach {
                uut.setInputState(InputState.Hidden())
            }

            it("sets .inputState.value to passed state of InputState.Hidden()") {
                uut.inputState.value!!::class shouldBe InputState.Hidden::class
            }
        }

        describe("when .setInputFocusState()") {
            beforeEach {
                uut.setInputFocusState(InputFocusState.Focussed)
            }

            it("sets .inputFocusState.value to passed state InputFocusState.Focussed") {
                uut.inputFocusState.value!!::class shouldBe InputFocusState.Focussed::class
            }
        }

        describe("when .setHintString()") {
            beforeEach {
                uut.setHintString("someHintString")
            }

            it("sets .hintString.value to passed string of 'someHintString'") {
                uut.hintString.value shouldBe "someHintString"
            }
        }

        describe("when .setDataEntryFieldType()") {
            beforeEach {
                uut.setDataEntryFieldType(DataEntryFieldType.DEFAULT)
            }

            it("sets .hintString.value to passed DataEntryFieldType.DEFAULT") {
                uut.dataEntryFieldType.value shouldBe DataEntryFieldType.DEFAULT
            }
        }

        describe("resetEntryString") {
            beforeEach {
                uut.entryString.value = "someValue"
                uut.resetEntryString()
            }

            it("sets .entryString.value to an empty string") {
                uut.entryString.value shouldBe ""
            }
        }

        describe("when .setInputCanBeCleared()") {
            beforeEach {
                uut.setInputCanBeCleared(false)
            }

            it("sets .inputCanBeCleared.value to passed value of false") {
                uut.inputCanBeCleared.value shouldBe false
            }
        }

        describe("when .setEndIconMode()") {
            beforeEach {
                uut.setEndIconMode(1)
            }

            it("sets .setEndIconMode.value to passed value of 1") {
                uut.dataEntryEndIconMode.value shouldBe 1
            }
        }

        describe("when .getInputStateValue()") {
            var result: InputState? = null

            beforeEach {
                uut.inputState.value = InputState.Default()
                result = uut.getInputStateValue()
            }

            it("returns the current InputState value of InputState.Default()") {
                result!!::class shouldBe InputState.Default::class
            }
        }

        describe("when .getTextInputId()") {
            var result: Int? = null

            beforeEach {
                result = uut.getTextInputId()
            }

            it("returns a value of 0") {
                result shouldBe 0
            }
        }

        describe("when .setTextInputId()") {

            beforeEach {
                uut.setTextInputId(1234)
            }

            it("sets .textInputId to 1234") {
                uut?.getTextInputId() shouldBe 1234
            }
        }

        describe("when .getInputString()") {
            var result: String? = null

            beforeEach {
                uut.entryString.value = "someInputString"
                result = uut.getInputString()
            }

            it("returns 'someInputString'") {
                result!! shouldBe "someInputString"
            }
        }

        describe("when .getFieldType()") {
            var result: DataEntryFieldType? = null

            beforeEach {
                uut.dataEntryFieldType.value = DataEntryFieldType.DEFAULT

                result = uut.getFieldType()
            }

            it("returns DataEntryFieldType.DEFAULT") {
                result!! shouldBe DataEntryFieldType.DEFAULT
            }
        }

        describe("when .entryIsBlank()") {
            var result: Boolean? = null

            describe("when the .entryString.value is mull") {
                beforeEach {
                    uut.entryString.value = null
                    result = uut.entryIsBlank()
                }

                it("returns true") {
                    result shouldBe true
                }
            }

            describe("when the .entryString.value is blank") {
                beforeEach {
                    uut.entryString.value = ""
                    result = uut.entryIsBlank()
                }

                it("returns true") {
                    result shouldBe true
                }
            }

            describe("when the .entryString.value has a value") {
                beforeEach {
                    uut.entryString.value = "someValue"
                    result = uut.entryIsBlank()
                }

                it("returns false") {
                    result shouldBe false
                }
            }
        }

        describe("when .canBeValidated()") {
            var result: Boolean? = null

            describe("when .inputState is .Hidden") {
                beforeEach {
                    uut.inputState.value = InputState.Hidden()
                    result = uut.canBeValidated()
                }

                it("returns false") {
                    result shouldBe false
                }
            }

            describe("when .inputState is .Disabled") {
                beforeEach {
                    uut.inputState.value = InputState.Disabled()
                    result = uut.canBeValidated()
                }

                it("returns false") {
                    result shouldBe false
                }
            }

            describe("when .inputState is anything else") {
                beforeEach {
                    uut.inputState.value = InputState.Default()
                    result = uut.canBeValidated()
                }

                it("returns true") {
                    result shouldBe true
                }
            }
        }

        describe("when .deleteButtonTapped()") {
            beforeEach {
                uut.entryString.value = "someValue"
                uut.deleteButtonTapped()
            }

            describe("when .resetEntryString()") {
                it("sets .entryString.value to an empty string") {
                    uut.entryString.value shouldBe ""
                }
            }

            describe("when .setInputFocusState()") {
                it("sets .inputFocusState.value to InputFocusState.Focussed") {
                    uut.inputFocusState.value!!::class shouldBe InputFocusState.Focussed::class
                }
            }

            describe("when .setInputState()") {
                it("sets .inputState.value to InputState.Default()") {
                    uut.inputState.value!!::class shouldBe InputState.Default::class
                }
            }
        }
    }
})