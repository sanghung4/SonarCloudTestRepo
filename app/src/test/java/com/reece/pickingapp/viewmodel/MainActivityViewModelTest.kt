package com.reece.pickingapp.viewmodel

import android.content.Context
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.UserPreferences
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityModelTest : DescribeSpec({
    describe("MainActivityViewModel") {
        lateinit var uut: MainActivityViewModel
        lateinit var mockUserPreferences: UserPreferences
        lateinit var mockPickingApi: PickingApi
        lateinit var mockSplitQtyDTO: SplitQtyDTO
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher


        beforeEach {
            mockPickingApi = mockk()
            mockUserPreferences = mockk(relaxUnitFun = true) {
                every { getEmailId() } returns "someEmailId"
                every { getUsername() } returns "someUsername"
                every { getBranch() } returns "someBranch"
                every { getSplitQty() } returns mockk()
                every { getErrorLogs() } returns mockk()
            }
            mockSplitQtyDTO = mockk()
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            uut = MainActivityViewModel(mockUserPreferences,mockPickingApi)
        }

        describe("when .getUsername()") {
            var result: String? = null

            beforeEach {
                result = uut.getUsername()
            }

            it("calls to mockUserPreferences.getUsername() for 'username' ") {
                verify { mockUserPreferences.getUsername() }
            }
            it("returns a String") {
                result shouldNotBe null
            }
        }

        describe("when .getUserName()") {
            var result: String? = null

            beforeEach {
                result = uut.getUserName()
            }
            it("calls .getUsername()") {
                verify { mockUserPreferences.getUsername() }
            }
            it("returns a String") {
                result shouldNotBe null
            }
        }


        describe("when .getErrorLogs()") {
            var result: ArrayList<ErrorLogDTO>? = null

            beforeEach {
                every { mockUserPreferences.getErrorLogs()  } returns ArrayList<ErrorLogDTO>()
                result = uut.getErrorLogs()
            }
            it("calls .getErrorLogs()") {
                verify { mockUserPreferences.getErrorLogs() }
            }
            it("returns a ArrayList<ErrorLogDTO>") {
                result shouldNotBe null
            }

        }

        describe("setting data") {
            beforeEach {
                uut.setUsername("someUsername")
            }
            it("calls to mockUserPreferences.setUsername() for 'username' passing in 'someUsername'") {
                verify { mockUserPreferences.setUsername("someUsername") }
            }


            describe("when .setEmailId()") {
                beforeEach {
                    uut.setEmailId("someEmailId")
                }
                it("calls to mockUserPreferences.setEmailId() for 'email_id' passing in 'someEmailId'") {
                    verify { mockUserPreferences.setEmailId("someEmailId") }
                }
            }


            describe("when .setErrorLog()") {
                beforeEach {
                    uut.setErrorLog(mockk())
                }
                it("calls to userPreferences.setErrorLog(errorLog)") {
                    verify { mockUserPreferences.setErrorLog(any()) }
                }
            }



            describe("when .getEmailId()") {
                var result: String? = null

                beforeEach {
                    result = uut.getEmailId()
                }

                it("calls to mockUserPreferences.getEmailId() for 'email_id' ") {
                    verify { mockUserPreferences.getEmailId() }
                }
                it("returns a String") {
                    result shouldNotBe null
                }
            }

            describe("when .setBranch()") {
                beforeEach {
                    uut.setBranch("someBranch")
                }
                it("calls to mockUserPreferences.setBranch() for 'branch_id' passing in 'someBranch'") {
                    verify { mockUserPreferences.setBranch("someBranch") }
                }
            }

            describe("when .getBranch()") {
                var result: String? = null

                beforeEach {
                    result = uut.getBranch()
                }

                it("calls to mockUserPreferences.getBranch() for 'branch_id' ") {
                    verify { mockUserPreferences.getBranch() }
                }
                it("returns a String") {
                    result shouldNotBe null
                }
            }

            describe("Split Qty Workaround data"){
                describe("when saveSplitQty()"){
                    beforeEach {
                        uut.saveSplitQty(mockSplitQtyDTO)
                    }
                    it("calls to mockUserPreferences.saveSplitQty() passing in 'mockSplitQtyDTO'") {
                        verify { mockUserPreferences.saveSplitQty(mockSplitQtyDTO) }
                    }
                }

                describe("when deleteSplitQty()"){
                    beforeEach {
                        uut.deleteSplitQty()
                    }
                    it("calls to mockUserPreferences.deleteSplitQty()") {
                        verify { mockUserPreferences.deleteSplitQty() }
                    }
                }

                describe("when getSplitQty()"){
                    var result: SplitQtyDTO? = null
                    beforeEach {
                        result=uut.getSplitQty()
                    }
                    it("calls to mockUserPreferences.getBranch() for 'branch_id' ") {
                        verify { mockUserPreferences.getSplitQty() }
                    }
                    it("returns a SplitQtyDTO") {
                        result shouldNotBe null
                    }
                }

            }


        }

        describe("when .logoutOfBrowser()"){
            var mockSignInViewModel:SignInViewModel
            var mockContext : Context
            beforeEach {
                mockContext = mockk()
                mockSignInViewModel = mockk()
                coEvery { mockPickingApi.signOut(any()) } returns Unit
                uut.logoutOfBrowser(mockContext)
            }
            it("calls pickingApi signout"){
                mockContext = mockk()
                coVerify {  mockPickingApi.signOut(any())}
            }
        }

        describe("when .getUserPreference()"){
            var result: UserPreferences? = null
            beforeEach {
                result = uut.getUserPreference()
            }
            it("result should not be null"){
                result shouldNotBe null
            }
        }
    }
})