package com.reece.pickingapp.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.testingUtils.ExtensionMock
import com.reece.pickingapp.utils.extensions.errorMessage
import com.reece.pickingapp.utils.extensions.showMessage
import com.reece.pickingapp.utils.extensions.showProductMovedMessage
import com.reece.pickingapp.view.state.SnackBarState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ActivityServiceTest : DescribeSpec({
    describe("ActivityService") {
        lateinit var uut: ActivityService
        lateinit var mockActivity: Activity
        lateinit var mockNavController: NavController
        lateinit var userPreferences: UserPreferences

        beforeEach {
            mockActivity = mockk()
            mockNavController = mockk()
            userPreferences = mockk()

            uut = ActivityService()
        }

        describe("when .setUp()") {
            beforeEach {
                uut.setUp(mockActivity, mockNavController, userPreferences)
            }

            it("sets .activity to passed activity") {
                uut.activity shouldBe mockActivity
            }
            it("sets .navController to passed navController") {
                uut.navController shouldBe mockNavController
            }
        }



        describe("when .getString()") {
            var result: String? = null

            describe("when .activity has a value") {
                beforeEach {
                    every { mockActivity.getString(any()) } returns "someString"
                    uut.activity = mockActivity
                    result = uut.getString(1)
                }
                it("returns a String") {
                    result shouldMatch "someString"
                }
            }

            describe("when .activity is NULL") {

                beforeEach {
                    uut.activity = null
                    result = uut.getString(1)
                }
                it("returns a NULL String") {
                    result shouldBe null
                }
            }
        }

        describe("when .getString(resourceInt:, concat:)") {
            var result: String? = null

            describe("when .activity has a value") {
                beforeEach {
                    every {
                        mockActivity.getString(
                            any(),
                            any()
                        )
                    } returns "someString someConcatString"
                    uut.activity = mockActivity
                    result = uut.getString(1, concat = "someConcatString")
                }
                it("returns a String") {
                    result shouldMatch "someString someConcatString"
                }
            }

            describe("when .activity is NULL") {

                beforeEach {
                    uut.activity = null
                    result = uut.getString(1)
                }
                it("returns a NULL String") {
                    result shouldBe null
                }
            }
        }

        describe("when .getString(resourceInt:, concat1:, concat2:)") {
            var result: String? = null

            describe("when .activity has a value") {
                beforeEach {
                    every {
                        mockActivity.getString(
                            any(),
                            any(),
                            any()
                        )
                    } returns "someString someConcatString someOtherConcatString"
                    uut.activity = mockActivity
                    result = uut.getString(
                        1,
                        concat1 = "someConcatString",
                        concat2 = "someOtherConcatString"
                    )
                }
                it("returns a String") {
                    result shouldMatch "someString someConcatString someOtherConcatString"
                }
            }

            describe("when .activity is NULL") {

                beforeEach {
                    uut.activity = null
                    result = uut.getString(1)
                }
                it("returns a NULL String") {
                    result shouldBe null
                }
            }
        }

        describe("when .getInteger()") {
            var result: Int? = null

            describe("when .activity has a value") {
                beforeEach {
                    every { mockActivity.resources.getInteger(any()) } returns 1234
                    uut.activity = mockActivity
                    result = uut.getInteger(1)
                }
                it("returns a String") {
                    result shouldBe 1234
                }
            }

            describe("when .activity is NULL") {

                beforeEach {
                    uut.activity = null
                    result = uut.getInteger(1)
                }
                it("returns a NULL String") {
                    result shouldBe null
                }
            }
        }

        describe("when .hideKeyboard()") {
            lateinit var mockInputMethodManager: InputMethodManager

            beforeEach {
                mockInputMethodManager = mockk {
                    every { hideSoftInputFromWindow(any(), any()) } returns true
                }
                every { mockActivity.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager } returns mockInputMethodManager
                every { mockActivity.window.decorView.rootView.windowToken } returns mockk()
            }

            describe("when .activity has a value") {
                beforeEach {
                    uut.activity = mockActivity
                    uut.hideKeyboard()
                }

                it("calls .inputMethodService.hideSoftInputFromWindow()") {
                    verify { mockInputMethodManager.hideSoftInputFromWindow(any(), any()) }
                }
            }

            describe("when .activity is NULL") {

                beforeEach {
                    uut.activity = null
                    uut.hideKeyboard()
                }
                it("does not call .inputMethodService.hideSoftInputFromWindow()") {
                    verify(exactly = 0) {
                        mockInputMethodManager.hideSoftInputFromWindow(
                            any(),
                            any()
                        )
                    }
                }
            }
        }


        describe("when .showKeyboard()"){
            lateinit var mockInputMethodManager: InputMethodManager
            lateinit var mockContext: Context

            beforeEach {
                mockInputMethodManager = mockk {
                    every { showSoftInput(any(), any()) } returns true
                }
                mockContext=mockk()
                every { mockActivity.baseContext } returns mockContext
                every { mockContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager } returns mockInputMethodManager
                every { mockActivity.window.decorView.rootView.windowToken } returns mockk()
                every { mockActivity.currentFocus } returns mockk()


            }
            describe("when activity has a value") {
                beforeEach {
                    uut.activity = mockActivity
                    uut.showKeyboard()
                }

                it("calls .inputMethodService.showSoftInput()") {
                    verify { mockInputMethodManager.showSoftInput(any(), 1) }
                }
            }

            describe("when .activity is NULL") {

                beforeEach {
                    uut.activity = null
                    uut.showKeyboard()
                }
                it("does not call .inputMethodService.showSoftInput()") {
                    verify(exactly = 0) {
                        mockInputMethodManager.showSoftInput(
                            any(),
                            any()
                        )
                    }
                }
            }
        }

        describe("when .showMessage()") {
            lateinit var mockSnackbarState: SnackBarState

            beforeEach {
                ExtensionMock.activityExtensions()
                mockSnackbarState =
                    SnackBarState(type = SnackBarType.SUCCESS, message = "someMessage")
                every { mockActivity.showMessage(any(), any()) } returns Unit
                every { mockActivity.getString(any()) } returns "someString"
                uut.activity = mockActivity
                uut.showMessage(mockSnackbarState)
            }

            it("calls .activity.showMessage()") {
                verify {
                    mockActivity.showMessage(mockSnackbarState, Snackbar.LENGTH_INDEFINITE)
                }
            }
        }

        /*
          fun showProductMovedMessage(duration: Int = Snackbar.LENGTH_INDEFINITE) {
        activity?.showProductMovedMessage(duration)
    }
         */

        describe("when .showProductMovedMessage()") {

            beforeEach {
                ExtensionMock.activityExtensions()
                every { mockActivity.showProductMovedMessage(any()) } returns Unit
                every { mockActivity.getString(any()) } returns "someString"
                uut.activity = mockActivity
                uut.showProductMovedMessage()
            }

            it("calls .activity.showMessage()") {
                verify {
                    mockActivity.showProductMovedMessage()
                }
            }
        }


        describe("when .errorMessage()") {
            beforeEach {
                ExtensionMock.activityExtensions()
                every { mockActivity.errorMessage(any()) } returns Unit
                every { mockActivity.getString(any()) } returns "someString"
                uut.activity = mockActivity
                uut.errorMessage("someErrorMessage")
            }

            it("calls .activity.errorMessage()") {
                verify {
                    mockActivity.errorMessage("someErrorMessage")
                }
            }
        }
    }
})