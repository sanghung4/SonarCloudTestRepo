package com.reece.pickingapp.viewmodel

import android.text.Spanned
import com.reece.pickingapp.utils.SnackBarType
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.state.SnackBarState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CustomSnackBarViewModelTest: DescribeSpec({
    describe("CustomSnackBarViewModel") {
        lateinit var uut: CustomSnackBarViewModel
        lateinit var mockStringConverter: StringConverter
        val passedMessage = "someMessage"
        var dismissCallbackWascalled = false
        lateinit var mockSpanned: Spanned


        beforeEach {
            mockSpanned = mockk()
            mockStringConverter = mockk {
                every { convertToHtmlSpan(any()) } returns mockSpanned
            }
            uut = CustomSnackBarViewModel(
                SnackBarState(SnackBarType.SUCCESS, passedMessage),
                dismissCallback = { dismissCallbackWascalled = true },
                mockStringConverter
            )
        }

        describe("when .closeButtonTapped()") {
            beforeEach {
                uut.closeButtonTapped()
            }

            it("calls .dismissCallback()") {
                dismissCallbackWascalled shouldBe true
            }
        }

        describe("when .getMessage()") {
            var result: Spanned? = null

            beforeEach {
                result = uut.getMessage()
            }

            it("calls .stringConverter.convertToHtmlSpan()") {
                verify { mockStringConverter.convertToHtmlSpan(passedMessage) }
            }

            it("returns") {
                result shouldBe mockSpanned
            }

            describe("if there is no message"){
                beforeEach {
                    uut = CustomSnackBarViewModel(
                        SnackBarState(SnackBarType.SUCCESS, null),
                        dismissCallback = { dismissCallbackWascalled = true },
                        mockStringConverter
                    )
                    result = uut.getMessage()
                }

                it("returns null"){
                    result shouldBe null

                }

            }
        }
    }
})