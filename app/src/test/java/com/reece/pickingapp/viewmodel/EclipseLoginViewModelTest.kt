package com.reece.pickingapp.viewmodel

import com.apollographql.apollo.api.Response
import com.google.android.material.textfield.TextInputLayout
import com.reece.pickingapp.R
import com.reece.pickingapp.VerifyEclipseCredentialsMutation
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.EclipseLoginRepository
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.DataEntryFieldType
import com.reece.pickingapp.view.state.InputFocusState
import com.reece.pickingapp.view.state.InputState
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalCoroutinesApi::class)
class EclipseLoginViewModelTest : DescribeSpec({
    describe("EclipseLoginViewModel") {
        @MockK
        lateinit var uut: EclipseLoginViewModel
        lateinit var mockRepository: PickingRepositoryImpl
        lateinit var mockEclipseLoginRepository: EclipseLoginRepository
        lateinit var mockResponse:  Response<VerifyEclipseCredentialsMutation.Data>
        lateinit var mockData: VerifyEclipseCredentialsMutation.Data
        lateinit var mockVerifyCredentials: VerifyEclipseCredentialsMutation.VerifyEclipseCredentials
        lateinit var mockUserPreferences: UserPreferencesImp
        lateinit var mockActivityService: ActivityService
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher
        lateinit var mockPickingApi: PickingApi
        lateinit var navigationCallback: () -> Unit
        var navigationCallbackWasCalled = false
        lateinit var mockUsernameDataEntryViewModel: DataEntryViewModel
        lateinit var mockPasswordDataEntryViewModel: DataEntryViewModel
        @MockK
        lateinit var mockBranchDataEntryViewModel: DataEntryViewModel

        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            mockRepository = mockk()
            mockEclipseLoginRepository = mockk()
            mockUserPreferences = mockk()
            mockActivityService = MockActivityService().create()
            mockPickingApi = mockk()

            uut = EclipseLoginViewModel(
                pickingRepository = mockRepository,
                userPreferences = mockUserPreferences,
                pickingApi = mockPickingApi,
                activityService = mockActivityService,
                repository = mockEclipseLoginRepository
            )

            mockVerifyCredentials = mockk {
                every { success } returns true
            }
            mockData = mockk {
                every { verifyEclipseCredentials } returns mockVerifyCredentials
            }
            mockResponse = mockk {
                every { errors } returns null
                every { data } returns mockData
            }
            coEvery { mockRepository.mutationVerifyEclipseCredentials(any(),any()) } returns mockResponse
            every { mockUserPreferences.getBranch() } returns "1445"

        }

        describe("when .setUp()") {
            beforeEach {
                mockUsernameDataEntryViewModel = mockk {
                    every { setHintString(any()) } returns Unit
                    every { setDataEntryFieldType(any()) } returns Unit
                    every { setEndIconMode(any()) } returns Unit
                }
                mockPasswordDataEntryViewModel = mockk {
                    every { setHintString(any()) } returns Unit
                    every { setDataEntryFieldType(any()) } returns Unit
                    every { setEndIconMode(any()) } returns Unit
                }
                mockBranchDataEntryViewModel = mockk {
                    every { setHintString(any()) } returns Unit
                    every { setDataEntryFieldType(any()) } returns Unit
                    every { setEndIconMode(any()) } returns Unit
                }
                navigationCallback = { navigationCallbackWasCalled = true }
                every { mockUserPreferences.getBranch() } returns null
                uut.usernameDataEntryViewModel.value = mockUsernameDataEntryViewModel
                uut.passwordDataEntryViewModel.value = mockPasswordDataEntryViewModel
                uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel

                uut.setUp(navigationCallback = navigationCallback)
            }

            it("sets .navigationCallback") {
                uut.navigateToBranchEntryCallback shouldBe navigationCallback
            }

            describe("when .setInputFields()") {
                it("calls .usernameDataEntryViewModel.setHintString()") {
                    verify { mockUsernameDataEntryViewModel.setHintString("Enter Username") }
                }
                it("calls .passwordDataEntryViewModel.setHintString()") {
                    verify { mockPasswordDataEntryViewModel.setHintString("Enter Password") }
                }
                it("calls .usernameDataEntryViewModel.setHintString()") {
                    verify { mockPasswordDataEntryViewModel.setDataEntryFieldType(DataEntryFieldType.PASSWORD) }
                }
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
                            every { setInputFocusState(any()) } returns Unit
                            every { setDataEntryFieldType(any()) } returns Unit

                        }
                        uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel
                        every { mockUserPreferences.getBranch() } returns "someBranchId"


                        uut.setUp(navigationCallback = navigationCallback)
                    }

                    it("sets .branchDataEntryViewModel entry string to 'someBranchId'") {
                        verify { mockBranchDataEntryViewModel.entryString.value = "someBranchId" }
                    }
                }
            }
        }

        describe("when .signInButtonTapped()") {
            mockActivityService.hideKeyboard()
            beforeEach {
                mockUsernameDataEntryViewModel = mockk {
                    every { setInputFocusState(any()) } returns Unit
                    every { entryString.value } returns "someUsername"
                    every { resetEntryString() } returns Unit
                    every { setInputState(any()) } returns Unit
                }
                mockPasswordDataEntryViewModel = mockk {
                    every { setInputFocusState(any()) } returns Unit
                    every { entryString.value } returns "somePassword"
                    every { resetEntryString() } returns Unit
                    every { setInputState(any()) } returns Unit
                }
                mockBranchDataEntryViewModel = mockk {
                    every { setInputFocusState(any()) } returns Unit
                    every { entryString.value } returns "someBranch"
                    every { resetEntryString() } returns Unit
                    every { setInputState(any()) } returns Unit
                }
                uut.usernameDataEntryViewModel.value = mockUsernameDataEntryViewModel
                uut.passwordDataEntryViewModel.value = mockPasswordDataEntryViewModel
                uut.branchDataEntryViewModel.value = mockBranchDataEntryViewModel
                uut.signInButtonTapped()
                describe("when .setBranchInputState()") {
                    it("calls .branchDataEntryViewModel() setting it to Default") {
                        verify {
                            mockBranchDataEntryViewModel.setInputState(withArg { state ->
                                state::class shouldBe InputState.Disabled::class
                            })
                        }
                    }
                }
                //linkAccount
                describe("when .linkAccount()") {
                    describe("when .setLoaderState()") {
                        it("sets .loaderState to LoaderState.Loading") {
                            eventually(duration = delayDuration) {
                                uut.loaderState.value shouldBe LoaderState.Loading
                            }
                        }
                    }

                    describe("when .setAllInputFocusState()") {
                        it("calls .usernameDataEntryViewModel.setInputFocusState() setting to NonFocusable") {
                            eventually(duration = delayDuration) {
                                verify {
                                    mockUsernameDataEntryViewModel.setInputFocusState(
                                        InputFocusState.NonFocusable
                                    )
                                }
                            }
                        }
                        it("calls .passwordDataEntryViewModel.setInputFocusState() setting to NonFocusable") {
                            eventually(duration = delayDuration) {
                                verify {
                                    mockPasswordDataEntryViewModel.setInputFocusState(
                                        InputFocusState.NonFocusable
                                    )
                                }
                            }
                        }
                    }

                    describe("when .setAllInputFieldsInputState()") {
                        it("calls .usernameDataEntryViewModel.setInputState()") {
                            eventually(duration = delayDuration) {
                                verify {
                                    mockUsernameDataEntryViewModel.setInputState(withArg { state ->
                                        state::class shouldBe InputState.Disabled::class
                                    })
                                }
                            }
                        }
                        it("calls .passwordDataEntryViewModel.setInputState()") {
                            eventually(duration = delayDuration) {
                                verify {
                                    mockPasswordDataEntryViewModel.setInputState(withArg { state ->
                                        state::class shouldBe InputState.Disabled::class
                                    })
                                }
                            }
                        }
                    }

                    describe("when .pickingRepository.mutationLinkAccount()") {
                        describe("when .verifyEclipseCredentials is a success") {
                            describe("when the credential validation is a success") {
                                beforeEach {
                                    mockUsernameDataEntryViewModel = mockk {
                                        every { setInputState(any()) } returns Unit
                                        every { setInputFocusState(any()) } returns Unit
                                        every { entryString.value } returns "someUsername"
                                        every { resetEntryString() } returns Unit
                                    }
                                    mockPasswordDataEntryViewModel = mockk {
                                        every { setInputState(any()) } returns Unit
                                        every { setInputFocusState(any()) } returns Unit
                                        every { entryString.value } returns "somePassword"
                                        every { resetEntryString() } returns Unit
                                    }
                                    uut.usernameDataEntryViewModel.value = mockUsernameDataEntryViewModel
                                    uut.passwordDataEntryViewModel.value = mockPasswordDataEntryViewModel

                                    every { mockUserPreferences.setUsername(any()) } returns Unit
                                    navigationCallback = { navigationCallbackWasCalled = true }
                                    uut.navigateToBranchEntryCallback = navigationCallback

                                    uut.signInButtonTapped()
                                }

                                it("calls .userPreferences.setUsername()") {
                                    eventually(duration = delayDuration) {
                                        verify { mockUserPreferences.setUsername("SOMEUSERNAME") }
                                    }
                                }

                                describe("when .setLoaderState()") {
                                    it("sets .loaderState to LoaderState.Default") {
                                        eventually(duration = delayDuration) {
                                            uut.loaderState.value shouldBe LoaderState.Default
                                        }
                                    }
                                }

                                describe("when .setValidateEclipseResponseState()") {
                                    it("sets .validateEclipseResponseState to ResponseState.Success()") {
                                        eventually(duration = delayDuration) {
                                            uut.validateEclipseResponseState.value!!::class shouldBe ResponseState.Success::class
                                        }
                                    }
                                    it("calls .navigateToBranchEntryCallback()") {
                                        eventually(duration = delayDuration) {
                                            navigationCallbackWasCalled shouldBe true
                                        }
                                    }

                                    describe("when .clearAllFields()") {

                                        it("calls .usernameDataEntryViewModel.resetEntryString()") {
                                            eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
                                                verify { mockUserPreferences.setUsername("SOMEUSERNAME") }
                                            }
                                        }

                                        it("calls .passwordDataEntryViewModel.resetEntryString()") {
                                            eventually(duration =  50.toDuration(DurationUnit.SECONDS)) {
                                                verify { mockUserPreferences.setUsername("SOMEUSERNAME")   }

                                            }
                                        }
                                    }

                                    describe("when .setAllInputFocusState()") {
                                        it("calls .usernameDataEntryViewModel.setInputFocusState() setting to Default") {
                                            eventually(duration = delayDuration) {
                                                verify {
                                                    mockUsernameDataEntryViewModel.setInputFocusState(
                                                        InputFocusState.Default
                                                    )
                                                }
                                            }
                                        }
                                        it("calls .passwordDataEntryViewModel.setInputFocusState() setting to Default") {
                                            eventually(duration = delayDuration) {
                                                verify {
                                                    mockPasswordDataEntryViewModel.setInputFocusState(
                                                        InputFocusState.Default
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            describe("when the credential validation is a failure") {
                                beforeEach {
                                    every { mockUserPreferences.getErrorLogs() } returns ArrayList()
                                    every { mockUserPreferences.setErrorLog(allAny()) } returns Unit
                                    mockUsernameDataEntryViewModel = mockk {
                                        every { setInputState(any()) } returns Unit
                                        every { setInputFocusState(any()) } returns Unit
                                        every { entryString.value } returns "someUsername"
                                        every { resetEntryString() } returns Unit
                                    }
                                    mockPasswordDataEntryViewModel = mockk {
                                        every { setInputState(any()) } returns Unit
                                        every { setInputFocusState(any()) } returns Unit
                                        every { entryString.value } returns "somePassword"
                                        every { resetEntryString() } returns Unit
                                    }
                                    uut.usernameDataEntryViewModel.value = mockUsernameDataEntryViewModel
                                    uut.passwordDataEntryViewModel.value = mockPasswordDataEntryViewModel

                                    every {mockUserPreferences.getErrorLogs() } returns ArrayList<ErrorLogDTO>()

                                    mockVerifyCredentials = mockk {
                                        every { success } returns false
                                        every { message } returns "someErrorMessage"
                                    }
                                    mockData = mockk {
                                        every { verifyEclipseCredentials } returns mockVerifyCredentials
                                    }
                                    mockResponse = mockk {
                                        every { errors } returns null
                                        every { data } returns mockData
                                    }
                                    coEvery { mockRepository.mutationVerifyEclipseCredentials(any(),any()) } returns mockResponse

                                    uut.signInButtonTapped()
                                }

                                describe("when .setErrorLog()") {

                                    describe("when .userPreferences.getErrorLogs().isEmpty() is a success)") {
                                        val errorLog = ArrayList<ErrorLogDTO>()
                                        errorLog.add(ErrorLogDTO(""))
                                        it("calls .userPreferences.seErrorLogs()") {
                                            eventually(duration = delayDuration) {
                                                verify { mockUserPreferences.setErrorLog(any()) }
                                            }
                                        }
                                    }
                                }

                                describe("when .setLoaderState()") {
                                    it("sets .loaderState to LoaderState.Default") {
                                        eventually(duration = delayDuration) {
                                            uut.loaderState.value shouldBe LoaderState.Default
                                        }
                                    }
                                }

                                describe("when .setValidateEclipseResponseState()") {
                                    it("sets .validateEclipseResponseState to ResponseState.Error()") {
                                        eventually(duration = delayDuration) {
                                            uut.validateEclipseResponseState.value!!::class shouldBe ResponseState.Error::class
                                        }
                                    }
                                    describe("when .clearAllFields()") {
                                        it("calls .usernameDataEntryViewModel.resetEntryString()") {
                                            eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
                                                verify { mockUsernameDataEntryViewModel.resetEntryString() }
                                            }
                                        }
                                        it("calls .passwordDataEntryViewModel.resetEntryString()") {
                                            eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
                                                verify { mockPasswordDataEntryViewModel.resetEntryString() }
                                            }
                                        }
                                    }
                                    describe("when .setAllInputFocusState()") {
                                        it("calls .usernameDataEntryViewModel.setInputFocusState() setting to Default") {
                                            eventually(duration = delayDuration) {
                                                verify {
                                                    mockUsernameDataEntryViewModel.setInputFocusState(
                                                        InputFocusState.Default
                                                    )
                                                }
                                            }
                                        }
                                        it("calls .passwordDataEntryViewModel.setInputFocusState() setting to Default") {
                                            eventually(duration = delayDuration) {
                                                verify {
                                                    mockPasswordDataEntryViewModel.setInputFocusState(
                                                        InputFocusState.Default
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    it("calls .activityService.errorMessage()") {
                                        eventually(duration = 50.toDuration(DurationUnit.SECONDS) ){
                                            verify { mockActivityService.errorMessage("someErrorMessage") }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


        }

        describe("when .backButtonSignOut()") {
            beforeEach {
                coEvery { mockActivityService.activity?.let { it1 -> mockPickingApi.signOut(it1) } } returns Unit
                uut.backButtonSignOut()
            }

            it("calls .pickingApi.signOut()") {
                coVerify { mockActivityService.activity?.let { mockPickingApi.signOut(it) } }
            }
        }


        describe("when onCheckedChange()"){
            beforeEach {
                uut.onCheckedChange(null, true)
            }
            it("calls eclipseLoginCheckBox.value = check "){
                assert(uut.eclipseLoginCheckBox.value == true)
            }
        }


        describe("when isAnyEclipseUserSaved () returns a valid username"){
            var result:Boolean = false
            beforeEach {
                every { mockUserPreferences.getUsername() } returns "SomeUserName"
                result = uut.isAnyEclipseUserSaved()

            }

            it("result is true"){
                assert(result == true)
            }
        }

        describe("when isAnyEclipseUserSaved () returns an empty username"){
            var result:Boolean = false
            beforeEach {
                every { mockUserPreferences.getUsername() } returns ""
                result = uut.isAnyEclipseUserSaved()

            }

            it("result is false"){
                assert(result == false)
            }
        }

        describe("when isAnyEclipseUserSaved () returns a null result"){
            var result:Boolean = false
            beforeEach {
                every { mockUserPreferences.getUsername() } returns null
                result = uut.isAnyEclipseUserSaved()

            }

            it("result is false"){
                assert(result == false)
            }
        }

    }
})