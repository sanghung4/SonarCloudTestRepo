package com.reece.pickingapp.viewmodel

import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.ValidateBranchQuery
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.view.state.InputState
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class BranchViewModelTest : DescribeSpec({
    describe("BranchViewModel") {
        lateinit var uut: BranchViewModel
        lateinit var mockUserPreferences: UserPreferences
        lateinit var mockActivityService: ActivityService
        lateinit var navigationCallback: () -> Unit
        var navigationCallbackWasCalled = false
        lateinit var mockBranchDataEntryViewModel: DataEntryViewModel
        lateinit var mockPickingApi: PickingApi
        lateinit var mockRepository: PickingRepositoryImpl
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher

        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            mockActivityService = MockActivityService().create()
            mockUserPreferences = mockk()
            mockPickingApi = mockk()
            mockRepository = mockk()

            uut = BranchViewModel(
                userPreferences = mockUserPreferences,
                activityService = mockActivityService,
                repository = mockRepository
            )
        }

        afterEach {
            Dispatchers.resetMain()
            mainThreadSurrogate.close()
        }

        describe("when .setUp") {

            beforeEach {
                navigationCallback = { navigationCallbackWasCalled = true }
                mockBranchDataEntryViewModel = mockk {
                    every { setHintString(any()) } returns Unit
                    every { setEndIconMode(any()) } returns Unit
                }
                uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel
                every { mockUserPreferences.getBranch() } returns null

                uut.setUp(navigationCallback = navigationCallback)
            }

            it("sets .navigateToOrdersCallback to passed navigationCallback") {
                uut.navigateToOrdersCallback shouldBe navigationCallback
            }

            describe("when .setInputFields()") {
                it("calls .branchDataEntryViewModel.setHintString()") {
                    verify { mockBranchDataEntryViewModel.setHintString("Branch ID") }
                }
                it("calls .branchDataEntryViewModel.setEndIconMode()") {
                    verify { mockBranchDataEntryViewModel.setEndIconMode(TextInputLayout.END_ICON_NONE) }
                }

                describe("when a branchId has been saved") {
                    beforeEach {
                        navigationCallback = { navigationCallbackWasCalled = true }
                        mockBranchDataEntryViewModel = mockk {
                            every { setHintString(any()) } returns Unit
                            every { setEndIconMode(any()) } returns Unit
                            every { entryString.getValue() } returns "someBranchId"
                            every { entryString.setValue(any()) } returns Unit
                            every { getInputString() } returns "someBranchId"
                            every { setInputState(any()) } returns Unit
                        }
                        uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel
                        every { mockUserPreferences.getBranch() } returns "someBranchId"


                        uut.setUp(navigationCallback = navigationCallback)
                    }

                    it("sets .branchDataEntryViewModel entry string to 'someBranchId'") {
                        verify { mockBranchDataEntryViewModel.entryString.value = "someBranchId" }
                    }

                    describe("when .validateBranchInput()") {
                        describe("when .setSubmitButtonEnabled()") {
                            it("sets .submitButtonEnabled to true") {
                                uut.submitButtonEnabled.value shouldBe true
                            }
                        }

                        describe("when .setBranchInputState()") {
                            it("calls .branchDataEntryViewModel() setting it to Default") {
                                verify {
                                    mockBranchDataEntryViewModel.setInputState(withArg { state ->
                                        state::class shouldBe InputState.Default::class
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }

        describe("when .afterTextChanged()") {
            describe("when id > 0") {
                describe("when .validateBranchInput()") {
                    describe("when .branchDataEntryViewModel.getInputString() length > minLength") {
                        beforeEach {
                            mockBranchDataEntryViewModel = mockk {
                                every { setHintString(any()) } returns Unit
                                every { setEndIconMode(any()) } returns Unit
                                every { setInputState(any()) } returns Unit
                                every { getInputString() } returns "123"
                            }
                            uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel

                            every { mockActivityService.getInteger(any()) } returns 3

                            uut.afterTextChanged(1, mockk())
                        }

                        describe("when .setSubmitButtonEnabled()") {
                            it("sets .submitButtonEnabled to true") {
                                uut.submitButtonEnabled.value shouldBe true
                            }
                        }

                        describe("when .setBranchInputState()") {
                            it("calls .branchDataEntryViewModel() setting it to Default") {
                                verify {
                                    mockBranchDataEntryViewModel.setInputState(withArg { state ->
                                        state::class shouldBe InputState.Default::class
                                    })
                                }
                            }
                        }
                    }

                    describe("when .branchDataEntryViewModel.getInputString() length is NOT > minLength") {

                        beforeEach {
                            every { mockActivityService.getInteger(any()) } returns 3
                            mockBranchDataEntryViewModel = mockk {
                                every { setHintString(any()) } returns Unit
                                every { setEndIconMode(any()) } returns Unit
                                every { setInputState(any()) } returns Unit
                                every { getInputString() } returns "12"
                            }
                            uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel

                            uut.afterTextChanged(1, mockk())
                        }

                        describe("when .setSubmitButtonEnabled()") {
                            it("sets .submitButtonEnabled to true") {
                                uut.submitButtonEnabled.value shouldBe false
                            }
                        }

                        describe("when .setBranchInputState()") {
                            it("calls .branchDataEntryViewModel() setting it to Error with message 'Branch Id is a minimum of 3 digits'") {
                                verify {
                                    mockBranchDataEntryViewModel.setInputState(withArg { state ->
                                        state::class shouldBe InputState.Error::class
                                        state.message shouldBe "Branch Id is a minimum of 3 digits"
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }

        describe("when .submitBranchId()") {
            beforeEach {
                mockUserPreferences = mockk()
                mockBranchDataEntryViewModel = mockk {
                    every { getInputString() } returns "someBranchId"
                }
                uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel
                every { mockUserPreferences.setBranch(any()) } returns Unit
                navigationCallback = { navigationCallbackWasCalled = true }
                uut.navigateToOrdersCallback = navigationCallback

                uut.submitBranchId()
            }

            it("calls .userPreferences.setBranch() passing the branchId") {
                eventually(duration = delayDuration) {
                    mockUserPreferences.setBranch("someBranchId")
                }
            }

            it("calls .navigateToOrdersCallback()") {
                uut.navigateToOrdersCallback shouldBe navigationCallback
            }

            describe("when .queryValidateBranch()") {
                lateinit var mockResponse: Response<ValidateBranchQuery.Data>

                beforeEach {
                    coEvery { mockRepository.queryValidateBranch(any()) } returns null
                    uut.queryValidateBranch("1234")
                }

                describe("when .setLoaderState()") {
                    it("sets .loaderState to Loading") {
                        eventually(duration = delayDuration) {
                            uut.loaderState.value shouldBe LoaderState.Loading
                        }
                    }
                }

                describe("when .setValidateBranchResponseState()") {
                    it("sets .validateBranchResponseState to Default") {
                        eventually(duration = delayDuration) {
                            uut.validateBranchResponseState.value!!::class shouldBe ResponseState.Default::class
                        }
                    }
                }

                describe("when the response does NOT have errors") {
                    lateinit var mockData: ValidateBranchQuery.Data
                    lateinit var mockValidateBranch: ValidateBranchQuery.ValidateBranch

                    beforeEach {
                        mockData = mockk()
                        mockResponse = mockk {
                            every { errors } returns null
                            every { data } returns mockData
                        }
                        coEvery { mockRepository.queryValidateBranch(any())} returns mockResponse
                        uut.queryValidateBranch("4321")
                    }

                    describe("when .setLoaderState()") {
                        it("sets .loaderState to Default") {
                            eventually(duration = delayDuration) {
                                uut.loaderState.value shouldBe LoaderState.Default
                            }
                        }
                    }

                    describe("when .setValidateBranchResponseState()") {

                        it("sets .validateBranchResponseState to Success") {
                            eventually(duration = delayDuration) {
                                uut.validateBranchResponseState.value!!::class shouldBe ResponseState.Success::class
                            }
                        }

                        it("sets .validateBranchResponseState.response to the call's response") {
                            eventually(duration = delayDuration) {
                                uut.validateBranchResponseState.value?.response shouldBe mockResponse
                            }
                        }

                        describe("when the response is valid branch") {
                            beforeEach {
                                mockValidateBranch = ValidateBranchQuery.ValidateBranch(
                                    isValid = true,
                                    branch = ValidateBranchQuery.Branch(
                                        branchId = "1234",
                                        branchName = "WMSAPPDV"
                                    )
                                )

                                mockData = mockk {
                                    every { validateBranch } returns mockValidateBranch
                                }
                                mockResponse = mockk {
                                    every { errors } returns null
                                    every { data } returns mockData
                                }

                                coEvery {
                                    mockRepository.queryValidateBranch(
                                        any()
                                    )
                                } returns mockResponse

                                uut.queryValidateBranch("1234")
                            }
                        }


                        describe("when the response does not have valid branch") {
                            beforeEach {
                                mockValidateBranch = ValidateBranchQuery.ValidateBranch(
                                    isValid = false,
                                    branch = null
                                )
                                mockData = mockk {
                                    every { validateBranch } returns mockValidateBranch
                                }
                                mockResponse = mockk {
                                    every { errors } returns null
                                    every { data } returns mockData
                                }

                                coEvery {
                                    mockRepository.queryValidateBranch(
                                        any()
                                    )
                                } returns mockResponse
                                uut.queryValidateBranch("4321")
                            }
                        }
                    }
                }
            }
        }
    }
})
