package com.reece.pickingapp.viewmodel
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.google.android.material.snackbar.Snackbar
import com.okta.authfoundation.AuthFoundationDefaults
import com.okta.authfoundation.claims.email
import com.okta.authfoundation.client.OidcClient
import com.okta.authfoundation.client.OidcConfiguration
import com.okta.authfoundation.client.SharedPreferencesCache
import com.okta.authfoundation.credential.CredentialDataSource.Companion.createCredentialDataSource
import com.okta.authfoundationbootstrap.CredentialBootstrap
import com.okta.webauthenticationui.WebAuthenticationClient.Companion.createWebAuthenticationClient
import com.reece.pickingapp.BuildConfig
import com.reece.pickingapp.CompleteUserPickMutation
import com.reece.pickingapp.R
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.models.SplitQtyDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.extensions.showMessage
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.SnackBarState
import com.reece.pickingapp.wrappers.OktaWebAuthWrapper
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
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import okhttp3.HttpUrl.Companion.toHttpUrl
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalCoroutinesApi::class)
class BackOrderViewModelTest : DescribeSpec( {

    describe("BackOrderViewModelTest"){
        lateinit var uut: BackOrderViewModel
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher

        lateinit var mockActivityService: ActivityService
        lateinit var mockPickingRepository: PickingRepositoryImpl
        lateinit var mockApplication :Application
        lateinit var mockContext: Context
        lateinit var mockSplitQtyDTO: SplitQtyDTO
        lateinit var mockProduct : MutableLiveData<ProductModel>
        lateinit var mockProductModel : ProductModel
        @MockK
        lateinit var mockActivity: Activity
        lateinit var mockNavController: NavController
        lateinit var mockSnackBarState : SnackBarState

        lateinit var mockResponse: Response<CompleteUserPickMutation.Data>
        val actionGlobalAuthNavigationValue = R.id.action_global_auth_navigation
        lateinit var mockNavigateAfterStagingCallback: () -> Unit




        @MockK
        lateinit var mockPickingApi : PickingApi
        lateinit var mockUserPreferences: UserPreferencesImp
        lateinit var mockMutableListProducts : MutableList<ProductModel?>
        lateinit var mockRepository : PickingRepository

        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)
            mockActivity = mockk ()
            mockNavController = mockk{
                every { navigate(actionGlobalAuthNavigationValue) } returns Unit
            }
            mockNavigateAfterStagingCallback = mockk()
            mockActivityService = mockk {
                every { activity } returns mockActivity
                every { navController } returns mockNavController
                every { getString(any()) } returns "someString"
                every { showMessage(any(), any()) } returns Unit
            }
            mockContext = mockk()
            mockProductModel = mockk()
            mockProduct = mockk{
                every { value } returns mockProductModel
            }
            mockPickingRepository = mockk()

            mockUserPreferences= mockk{
                every { setEmailId(any()) } returns Unit

            }
            mockPickingApi = mockk()
            mockApplication = mockk()
            mockMutableListProducts = mutableListOf()

            mockSplitQtyDTO = mockk{
                every { productsList } returns mockMutableListProducts
            }
            mockResponse = mockk()
            mockSnackBarState = mockk()
            mockPickingRepository = mockk()

            uut = BackOrderViewModel(
                userPreferences = mockUserPreferences,
                activityService = mockActivityService,
                repository =mockPickingRepository
            )

        }
        describe("when .saveBackOrder()"){
            beforeEach {
                every{mockUserPreferences.getSplitQty()} returns mockSplitQtyDTO
                every{mockUserPreferences.saveSplitQty(mockSplitQtyDTO)} returns Unit
                mockNavigateAfterStagingCallback = {  }
                uut.navigateAfterStagingCallback = mockNavigateAfterStagingCallback

                uut.saveBackOrder()
            }
            it("calls postAlertAndNavigateBack()"){
                verify {
                    mockActivityService.showMessage(any(),Snackbar.LENGTH_SHORT)
                }
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

        /*
        fun yesButtonTapped() {
        _showProgressBar.value = 1
        submitPickItem(product.value!!)
    }
         */

       /* describe("when .yesButtonTapped()"){
            beforeEach {
                uut.product.value = mockProductModel
                uut.yesButtonTapped()
            }
            it("calls submitPickItem()"){
                verify { uut.submitPickItem(uut.product.value!!) }
            }
        }*/


    }
})