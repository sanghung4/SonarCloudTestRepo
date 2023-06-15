package com.reece.pickingapp.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.navigation.NavController
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.google.android.material.snackbar.Snackbar
import com.reece.pickingapp.CompleteUserPickMutation
import com.reece.pickingapp.R
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.adapter.PickFromOtherLocationsAdapter
import com.reece.pickingapp.view.state.PickFromOtherLocationsState
import com.reece.pickingapp.wrappers.OktaWebAuthWrapper
import com.reece.pickingapp.wrappers.PickFromOtherLocationsAdapterWrapper
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalCoroutinesApi::class)
class PickFromOtherLocationsViewModelTest : DescribeSpec( {

    describe("PickFromOtherLocationsViewModelTest"){
        lateinit var uut: PickFromOtherLocationsViewModel
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher

        lateinit var mockActivityService: ActivityService
        lateinit var mockApplication : Application
        lateinit var mockContext: Context
        lateinit var mockOktaWebAuthWrapper: OktaWebAuthWrapper
        lateinit var mockSignInViewModelBrowserState : SignInViewModel.BrowserState
        lateinit var mockPickFromOtherLocationsAdapterWrapper : PickFromOtherLocationsAdapterWrapper
        lateinit var mockStringConverter : StringConverter
        lateinit var mockNavigateAfterStagingCallback: () -> Unit


        @MockK
        lateinit var mockPickingApi : PickingApi
        lateinit var mockPickingRepository: PickingRepository
        lateinit var mockUserPreferences: UserPreferencesImp
        lateinit var mockRepository : PickingRepository
        lateinit var mockMutableListProducts : MutableList<ProductModel?>
        lateinit var mockSplitQtyDTO: SplitQtyDTO
        lateinit var mockProductModel: ProductModel
        lateinit var mockActivity: Activity
        lateinit var mockNavController: NavController
        val actionGlobalAuthNavigationValue = R.id.action_global_auth_navigation

        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)
            mockActivity = mockk ()
            mockNavController = mockk{
                every { navigate(actionGlobalAuthNavigationValue) } returns kotlin.Unit
            }

            mockActivityService = mockk {
                every { activity } returns mockActivity
                every { navController } returns mockNavController
                every { getString(any()) } returns "someString"
                every { showMessage(any(), any()) } returns Unit
            }
            mockContext = mockk()
            mockRepository = mockk()

            mockProductModel = mockk()


            mockUserPreferences= mockk{
                every { setEmailId(any()) } returns Unit

            }
            mockPickingApi = mockk()
            mockApplication = mockk()
            mockMutableListProducts = mutableListOf()

            mockSplitQtyDTO = mockk{
                every { productsList } returns mockMutableListProducts
            }
            mockSignInViewModelBrowserState = mockk()
            mockPickingRepository = mockk()
            mockStringConverter = mockk()
            mockPickFromOtherLocationsAdapterWrapper = mockk()

            uut = PickFromOtherLocationsViewModel(
                repository = mockPickingRepository,
                userPreferences = mockUserPreferences,
                activityService = mockActivityService,
                stringConverter = mockStringConverter,
                pickFromOtherLocationsAdapterWrapper = mockPickFromOtherLocationsAdapterWrapper
            )


            mockOktaWebAuthWrapper = mockk {
                coEvery { getToken() } returns "someToken"
                coEvery { signOut(mockContext){mockPickingApi.signOutCallBack()} } returns Unit
                coEvery { refreshOktaToken() } returns Unit
                coEvery { refreshOktaToken(any(), any()) } returns Unit
            }


        }
        describe("when .submitPickItem()") {

            lateinit var mockProduct: ProductModel
            var failureCallbackWasCalled = false
            lateinit var mockProductDTO: ProductDTO

            beforeEach {
                mockProductDTO = mockk()

                mockProduct = mockk {
                    every { setStartPickTime(any()) } returns Unit
                    every { setTote() } returns Unit
                    every { getProductDTO() } returns mockProductDTO
                }


                uut.submitPickItem(
                    mockProduct
                )
            }

            it("calls the product's .setStartPickTime()") {
                eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
                    verify { mockProduct.setStartPickTime(any()) }
                }
            }
            it("calls the product's .setTote()") {
                eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
                    verify { mockProduct.setTote() }
                }
            }

            describe("when .repository.mutationCompletePick()") {
                lateinit var mockResponse: Response<CompleteUserPickMutation.Data>

                describe("when the response does NOT have errors") {
                    lateinit var mockData: CompleteUserPickMutation.Data

                    beforeEach {
                        mockData = mockk()

                        mockResponse = mockk {
                            every { errors } returns null
                            every { data } returns mockData
                        }

                        coEvery {
                            mockRepository.mutationCompletePick(any())
                        } returns mockResponse


                        uut.submitPickItem(
                            mockProduct
                        )
                    }

                }

                describe("when the response has errors") {
                    lateinit var mockError: Error

                    beforeEach {
                        mockError = mockk {
                            every { message } returns "someErrorMessage"
                        }
                        mockResponse = mockk {
                            every { errors } returns listOf(mockError)
                        }

                        coEvery {
                            mockRepository.mutationCompletePick(any())
                        } returns mockResponse

                        every { mockActivityService.errorMessage(any()) } returns Unit
                        uut.submitPickItem(
                            mockProduct
                        )
                    }

                    describe("when .failureCallback()") {
                        eventually(duration = delayDuration) {
                            failureCallbackWasCalled shouldBe false
                        }
                    }
                }
            }
        }

        describe("when .saveProductOnUserPreferences()"){
            beforeEach {
                uut.productData = mockProductModel
                every{mockUserPreferences.getSplitQty()} returns mockSplitQtyDTO
                every{mockUserPreferences.saveSplitQty(mockSplitQtyDTO)} returns Unit
                mockNavigateAfterStagingCallback = {  }
                uut.navigateAfterStagingCallback = mockNavigateAfterStagingCallback

                uut.saveProductOnUserPreferences()
            }
            it("calls postAlertAndNavigateBack()"){
                verify {
                    mockActivityService.showMessage(any(), Snackbar.LENGTH_SHORT)
                }
            }
        }


        describe("when setPickFromOtherLocationState()"){
            var mockPickFromOtherLocationsState: PickFromOtherLocationsState = PickFromOtherLocationsState.ReadyToPick()

            beforeEach {
                uut.setPickFromOtherLocationState(mockPickFromOtherLocationsState)
            }
            it("sets default state"){
                assertTrue(uut.topErrorMessageState.value == mockPickFromOtherLocationsState)
                assertTrue(uut.bottomErrorMessageState.value == mockPickFromOtherLocationsState)
                assertTrue(uut.completeButtonState.value == mockPickFromOtherLocationsState)
                assertTrue(uut.pickFromOtherLocationsState.value == mockPickFromOtherLocationsState)
            }
        }





    }


})