package com.reece.pickingapp.viewmodel

import android.app.Activity
import android.content.SharedPreferences
import android.text.Spanned
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.reece.pickingapp.*
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.type.UpdateProductSerialNumbersInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.adapter.ProductsAdapter
import com.reece.pickingapp.view.state.*
import com.reece.pickingapp.wrappers.ProductsAdapterWrapper
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class ProductsFragmentViewModelTest : DescribeSpec({
    describe("ProductsFragmentViewModel") {
        lateinit var uut: ProductsFragmentViewModel
        lateinit var mockRepository: PickingRepositoryImpl
        lateinit var mockUserPreferences: UserPreferencesImp
        lateinit var mockActivityService: ActivityService
        lateinit var mockStringConverter: StringConverter
        lateinit var mockProductsAdapterWrapper: ProductsAdapterWrapper
        lateinit var mockLocationDataEntryViewModel: DataEntryViewModel
        lateinit var mockBoxesDataEntryViewModel: DataEntryViewModel
        lateinit var mockSkidsDataEntryViewModel: DataEntryViewModel
        lateinit var mockBundlesDataEntryViewModel: DataEntryViewModel
        lateinit var mockPiecesDataEntryViewModel: DataEntryViewModel
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher
        lateinit var mockNavigateAfterStagingCallback: () -> Unit
        var navigateAfterStagingCallbackWasCalled = false
        lateinit var mockResponse: Response<ShippingDetailsQuery.Data>
        lateinit var mockData: ShippingDetailsQuery.Data
        lateinit var mockErrorData:  ArrayList<ErrorLogDTO>
        lateinit var mockSharedPreferences: SharedPreferences
        lateinit var mockActivity: Activity



        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            mockRepository = mockk()
            mockUserPreferences = mockk {
                every { getErrorLogs() } returns ArrayList<ErrorLogDTO>()
            }
            mockErrorData=mockUserPreferences.getErrorLogs()


            mockActivityService = MockActivityService().create()
            mockStringConverter = mockk {
                every { convertToHtmlSpan(any()) } returns mockk()
            }
            mockProductsAdapterWrapper = mockk()

            mockLocationDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
            }
            mockBoxesDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }
            mockSkidsDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }
            mockBundlesDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }
            mockPiecesDataEntryViewModel = mockk {
                every {
                    getInputString()
                } returns "some value"
                every {
                    setInputState(any())
                } returns Unit
                every {
                    setHintString(any())
                } returns Unit
                every {
                    setDataEntryFieldType(any())
                } returns Unit
            }

            uut = ProductsFragmentViewModel(
                mockRepository,
                mockUserPreferences,
                mockActivityService,
                mockStringConverter,
                mockProductsAdapterWrapper
            )

            mockSharedPreferences = mockk {
                every { getString(any(), any()) } returns "someString"
                every { edit().putString(any(), any())?.apply() } returns Unit
            }
            mockActivity = mockk {
                every { getSharedPreferences(any(), any()) } returns mockSharedPreferences
            }
            mockActivityService = mockk {
                every { activity } returns mockActivity
            }

            mockUserPreferences = UserPreferencesImp(mockActivityService)
        }

        afterEach {
            Dispatchers.resetMain()
            mainThreadSurrogate.close()
        }

        describe("when .queryShippingDetails()") {
            lateinit var mockOrderViewModel: OrderViewModel
            beforeEach {
                mockOrderViewModel = mockk {
                    every { getOrderIdValue() } returns "someOrderId"
                }
                uut.selectedOrder.value = mockOrderViewModel

                uut.queryShippingDetails()
            }


            describe("when the response does NOT have errors") {
                beforeEach {
                    mockData = mockk()
                    mockResponse = mockk {
                        every { errors } returns null
                        every { data } returns mockData
                    }
                    coEvery { mockRepository.queryShippingDetails(any()) } returns mockResponse
                    uut.queryShippingDetails()
                }
                it("response should not be null'") {
                    mockResponse shouldNotBe null
                }
            }
        }

        describe("when showMoreOrLessButtonTapped() and shippingDetailsMaxLines is 1"){
            beforeEach {
                var mockViewModel : ProductsFragmentViewModel = mockk()
                every { mockViewModel.shippingDetailsMaxLines.value } returns 1
                uut.showMoreOrLessButtonTapped()
            }
            it("changes shippingDetailsMaxLines to 10"){
                assert(uut.shippingDetailsMaxLines.value == 10)
            }
        }


        //TODO: Later uncomment below code
//        describe("when .setUp()") {
//            lateinit var mockOrderViewModel: OrderViewModel
//            lateinit var mockLifecycleOwner: LifecycleOwner
//            lateinit var mockProductsAdapter: ProductsAdapter
//
//            beforeEach {
//                val mockLooper: Looper = mockk()
//                mockkStatic(Looper::class)
//                every { Looper.myLooper() } returns mockLooper
//                every { Looper.getMainLooper() } returns mockLooper
//
//                mockOrderViewModel = mockk {
//                    every { branchId } returns "someBranchId"
//                    every { assignedUserId } returns "someAssignedUserId"
//                    every { orderId } returns "someOrderId"
//                }
//                mockLifecycleOwner = mockk()
//                mockProductsAdapter = mockk()
//
//                every { mockProductsAdapterWrapper.createAdapter(any()) } returns mockProductsAdapter
//
//                uut.locationDataEntryViewModel.value = mockLocationDataEntryViewModel
//                uut.boxesDataEntryViewModel.value = mockBoxesDataEntryViewModel
//                uut.skidsDataEntryViewModel.value = mockSkidsDataEntryViewModel
//                uut.bundlesDataEntryViewModel.value = mockBundlesDataEntryViewModel
//                uut.piecesDataEntryViewModel.value = mockPiecesDataEntryViewModel
//
//                mockNavigateAfterStagingCallback = {}
//
//                uut.setUp(
//                    mockOrderViewModel,
//                    mockLifecycleOwner,
//                    mockNavigateAfterStagingCallback
//                )
//            }
//
//            it("sets .viewLifecycleOwner to passed lifecycleOwner") {
//                uut.viewLifecycleOwner shouldBe mockLifecycleOwner
//            }
//
//            it("sets .fragment to passed weakFragment") {
//                uut.navigateAfterStagingCallback shouldBe mockNavigateAfterStagingCallback
//            }
//
//            describe("when .setSelectedOrder()") {
//                it("sets .selectedOrder to passed OrderViewModel") {
//                    uut.selectedOrder.value shouldBe mockOrderViewModel
//                }
//            }
//
////            describe("when .queryPickTasks()") {
////                describe("when .selectedOrder has a value") {
////                    describe("when .showQueryPickTaskLoading is true") {
//////                        describe("when .setLoaderState()") {
////////                            it("sets .loaderState to LoaderState.Loading") {
//////////                                uut.loaderState.value shouldBe LoaderState.Loading
////////                            }
//////                        }
////
////                        describe("when .setProductsResponseState()") {
////                            it("sets .productsResponseState to ResponseState.Default()") {
////                                uut.productsResponseState.value!!::class shouldBe ResponseState.Default::class
////                            }
////                        }
////                    }
////                }
////            }
//
//            describe("when .setProductsResponseState()") {
//                it("sets .productsResponseState to ResponseState.Default()") {
//                    uut.productsResponseState.value!!::class shouldBe ResponseState.Default::class
//                }
//
//                it("sets .activeResponseState to .productsResponseState") {
//                    uut.activeResponseState.value!!::class shouldBe uut.productsResponseState.value!!::class
//                }
//            }
//
//            it("sets .productAdapter") {
//                verify { mockProductsAdapterWrapper.createAdapter(mockLifecycleOwner) }
//            }
//
//            describe("when .setStagingOrderState()") {
//                it("sets .stagingState to StagingOrderState.Default") {
//                    uut.stagingState.value!!::class shouldBe StagingOrderState.Default::class
//                }
//            }
//
//            describe("when .setDataEntryInputState()") {
//                it("calls .locationDataEntryViewModel.setInputState() passing InputState.Default()") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockLocationDataEntryViewModel.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Default::class
//                            })
//                        }
//                    }
//                }
//                it("calls .boxesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBoxesDataEntryViewModel.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Default::class
//                            })
//                        }
//                    }
//                }
//                it("calls .skidsDataEntryViewModel.setInputState() passing InputState.Default()") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockSkidsDataEntryViewModel.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Default::class
//                            })
//                        }
//                    }
//                }
//                it("calls .bundlesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBundlesDataEntryViewModel.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Default::class
//                            })
//                        }
//                    }
//                }
//                it("calls .piecesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockPiecesDataEntryViewModel.setInputState(withArg { state ->
//                                state::class shouldBe InputState.Default::class
//                            })
//                        }
//                    }
//                }
//            }
//
//            describe(" when .setDataEntryFieldType") {
//                it("calls .boxesDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBoxesDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//                it("calls .skidsDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockSkidsDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//                it("calls .bundlesDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBundlesDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//                it("calls .piecesDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockPiecesDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//            }
//
//            describe(" when .setDataEntryFieldType") {
//                it("calls .boxesDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBoxesDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//                it("calls .skidsDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockSkidsDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//                it("calls .bundlesDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBundlesDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//                it("calls .piecesDataEntryViewModel.setDataEntryFieldType() passing a DataEntryFieldType") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockPiecesDataEntryViewModel.setDataEntryFieldType(any())
//                        }
//                    }
//                }
//            }
//
//            describe("when .setDataEntryHints()") {
//                it("calls .locationDataEntryViewModel.setHintString() passing a String") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockLocationDataEntryViewModel.setHintString("Staging Location *")
//                        }
//                    }
//                }
//                it("calls .boxesDataEntryViewModel.setHintString() passing a String") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBoxesDataEntryViewModel.setHintString("Boxes")
//                        }
//                    }
//                }
//                it("calls .skidsDataEntryViewModel.setHintString() passing a String") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockSkidsDataEntryViewModel.setHintString("Skids")
//                        }
//                    }
//                }
//                it("calls .bundlesDataEntryViewModel.setHintString() passing a String") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockBundlesDataEntryViewModel.setHintString("Bundles")
//                        }
//                    }
//                }
//                it("calls .piecesDataEntryViewModel.setHintString() passing a String") {
//                    eventually(duration = delayDuration) {
//                        verify {
//                            mockPiecesDataEntryViewModel.setHintString("Pieces")
//                        }
//                    }
//                }
//            }
//        }

        describe("when .queryPickTasks()") {
            lateinit var mockOrderViewModel: OrderViewModel

            beforeEach {
                mockOrderViewModel = mockk {
                    every { branchId } returns "someBranchId"
                    every { assignedUserId } returns "someAssignedUserId"
                    every { orderId } returns "someOrderId"
                }
                uut.selectedOrder.value = mockOrderViewModel
            }

            describe("when .selectedOrder has a value") {
                describe("when .showQueryPickTaskLoading is true") {
                    beforeEach {
                        uut.showQueryPickTaskLoading = true
                        uut.queryPickTasks()
                    }

                    describe("when .setLoaderState()") {
                        it("sets .loaderState to LoaderState.Loading") {
                            eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
                                uut.loaderState.value shouldBe LoaderState.Loading
                            }
                        }
                    }

                    describe("when .setProductsResponseState()") {
                        it("sets .productsResponseState to ResponseState.Default()") {
                            eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
                                uut.productsResponseState.value!!::class shouldBe ResponseState.Default::class
                            }
                        }
                    }
                }

                describe("when .repository.queryUserTasks()") {
                    lateinit var mockOrderViewModel: OrderViewModel

                    beforeEach {
                        mockOrderViewModel = mockk {
                            every { branchId } returns "someBranchId"
                            every { assignedUserId } returns "someAssignedUserId"
                            every { orderId } returns "someOrderId"
                        }
                        uut.selectedOrder.value = mockOrderViewModel
                    }

                    lateinit var mockResponse: Response<PickTasksQuery.Data>

                    describe("when the response does NOT have errors") {
                        beforeEach {
                            mockResponse = mockk {
                                every { errors } returns null
                            }
                            coEvery {
                                mockRepository.queryUserTasks(
                                    any(),
                                    any(),
                                    any(),
                                )
                            } returns mockResponse
                            uut.queryPickTasks()
                        }

                        describe("when .setLoaderState()") {
                            it("sets .loaderState to LoaderState.Loading") {
                                eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
                                    uut.loaderState.value shouldBe LoaderState.Default
                                }
                            }
                        }

                        describe("when .setProductsResponseState()") {
                            it("sets .productsResponseState to ResponseState.Success()") {
                                eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
                                    uut.productsResponseState.value!!::class shouldBe ResponseState.Success::class
                                }
                            }

                            describe("when the passed response has UserPicks") {
                                lateinit var mockData: PickTasksQuery.Data
                                lateinit var mockPick: PickTasksQuery.UserPick
                                lateinit var mockProductsAdapter: ProductsAdapter
                                lateinit var mockLifecycleOwner: LifecycleOwner
                                beforeEach {
                                    mockLifecycleOwner = mockk()
                                    mockPick = mockk {
                                        every { productId } returns "someProductId"
                                        every { description } returns "some"
                                        every { quantity } returns 1
                                        every { uom } returns "some"
                                        every { locationType } returns "some"
                                        every { location } returns "some"
                                        every { lot } returns "some"
                                        every { splitId } returns "some"
                                        every { orderId } returns "some"
                                        every { generationId } returns 0
                                        every { lineId } returns 0
                                        every { shipVia } returns "some"
                                        every { tote } returns "some"
                                        every { userId } returns "some"
                                        every { branchId } returns "some"
                                        every { cutDetail } returns "some"
                                        every { cutGroup } returns "some"
                                        every { isParallelCut } returns false
                                        every { warehouseID } returns "some"
                                        every { isLot } returns "some"
                                        every { isSerial } returns false
                                        every { pickGroup } returns "some"
                                    }

                                    mockData = mockk {
                                        every { userPicks } returns listOf(mockPick)
                                    }

                                    mockResponse = mockk {
                                        every { errors } returns null
                                        every { data } returns mockData
                                    }

                                    coEvery {
                                        mockRepository.queryUserTasks(
                                            any(),
                                            any(),
                                            any()
                                        )
                                    } returns mockResponse

                                    mockProductsAdapter = mockk {
                                        every { submitList(any()) } returns Unit
                                    }

                                    every {
                                        mockProductsAdapterWrapper.createAdapter(any())
                                    } returns mockProductsAdapter

                                    uut.productsAdapter.value = mockProductsAdapter
                                    uut.viewLifecycleOwner = mockLifecycleOwner
                                    uut.queryPickTasks()
                                }
                                //TODO: Will fix this unit test case in task ERPCP-1023
//                                it("calls .productsAdapter.submitList()") {
//                                    eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                                        verify {
//                                            mockProductsAdapter.submitList(withArg {
//                                                it.count() shouldBe 1
//                                            })
//                                        }
//                                    }
//                                }
                            }

                            describe("when the passed response has no UserPicks") {
                                lateinit var mockData: PickTasksQuery.Data
                                lateinit var mockProductsAdapter: ProductsAdapter
                                lateinit var mockLifecycleOwner: LifecycleOwner
                                beforeEach {
                                    mockLifecycleOwner = mockk()

                                    mockData = mockk {
                                        every { userPicks } returns emptyList()
                                    }

                                    mockResponse = mockk {
                                        every { errors } returns null
                                        every { data } returns mockData
                                    }

                                    coEvery {
                                        mockRepository.queryUserTasks(
                                            any(),
                                            any(),
                                            any()
                                        )
                                    } returns mockResponse

                                    mockProductsAdapter = mockk {
                                        every { submitList(any()) } returns Unit
                                    }

                                    uut.productsAdapter.value = mockProductsAdapter
                                    uut.viewLifecycleOwner = mockLifecycleOwner

                                    uut.locationDataEntryViewModel.value =
                                        mockLocationDataEntryViewModel
                                    uut.boxesDataEntryViewModel.value = mockBoxesDataEntryViewModel
                                    uut.skidsDataEntryViewModel.value = mockSkidsDataEntryViewModel
                                    uut.bundlesDataEntryViewModel.value =
                                        mockBundlesDataEntryViewModel
                                    uut.piecesDataEntryViewModel.value =
                                        mockPiecesDataEntryViewModel

                                    uut.queryPickTasks()
                                }
                                it("calls .productsAdapter.submitList()") {
                                    eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
                                        verify {
                                            mockProductsAdapter.submitList(withArg {
                                                it.count() shouldBe 0
                                            })
                                        }
                                    }
                                }
                                describe("when .setFormVisibility()") {
                                    it("sets locationFormVisible to Visible") {
                                        eventually(duration = delayDuration) {
                                            uut.locationFormVisible.value shouldBe View.VISIBLE
                                        }
                                    }
                                }
                                  //TODO: Later uncomment below code
//                                describe("when .setDataEntryInputState()") {
//                                    it("calls .locationDataEntryViewModel.setInputState() passing InputState.Default()") {
//                                        eventually(duration = delayDuration) {
//                                            verify {
//                                                mockLocationDataEntryViewModel.setInputState(withArg { state ->
//                                                    state::class shouldBe InputState.Default::class
//                                                })
//                                            }
//                                        }
//                                    }
//                                    it("calls .boxesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                                        eventually(duration = delayDuration) {
//                                            verify {
//                                                mockBoxesDataEntryViewModel.setInputState(withArg { state ->
//                                                    state::class shouldBe InputState.Default::class
//                                                })
//                                            }
//                                        }
//                                    }
//                                    it("calls .skidsDataEntryViewModel.setInputState() passing InputState.Default()") {
//                                        eventually(duration = delayDuration) {
//                                            verify {
//                                                mockSkidsDataEntryViewModel.setInputState(withArg { state ->
//                                                    state::class shouldBe InputState.Default::class
//                                                })
//                                            }
//                                        }
//                                    }
//                                    it("calls .bundlesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                                        eventually(duration = delayDuration) {
//                                            verify {
//                                                mockBundlesDataEntryViewModel.setInputState(withArg { state ->
//                                                    state::class shouldBe InputState.Default::class
//                                                })
//                                            }
//                                        }
//                                    }
//                                    it("calls .piecesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                                        eventually(duration = delayDuration) {
//                                            verify {
//                                                mockPiecesDataEntryViewModel.setInputState(withArg { state ->
//                                                    state::class shouldBe InputState.Default::class
//                                                })
//                                            }
//                                        }
//                                    }
//                                }
                            }
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
                                mockRepository.queryUserTasks(
                                    any(),
                                    any(),
                                    any()
                                )
                            } returns mockResponse

                            every { mockActivityService.errorMessage("someErrorMessage") } returns Unit

                            uut.queryPickTasks()
                        }

                        describe("when .setProductsResponseState()") {
                            it("sets .productsResponseState to ResponseState.Error()") {
                                eventually(duration = delayDuration) {
                                    uut.productsResponseState.value!!::class shouldBe ResponseState.Error::class
                                }
                            }

                            describe("that Error response state") {
                                it("has a message of 'Error fetching products'") {
                                    eventually(duration = delayDuration) {
                                        uut.productsResponseState.value?.message shouldBe "Error fetching products"
                                    }
                                }
                            }
                        }

                        describe("when .setLoaderState()") {
                            it("sets .loaderState to LoaderState.Loading") {
                                eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
                                    uut.loaderState.value shouldBe LoaderState.Loading
                                }
                            }
                        }
                    }
                }
            }
        }

        describe("when .updateProductSerialNumbers()") {

            lateinit var mockUpdateProductSerialNumbersInput: UpdateProductSerialNumbersInput
            lateinit var mockProduct: ProductModel
            lateinit var mockFailureCallback: () -> Unit
            lateinit var mockResponse: Response<UpdateProductSerialNumbersMutation.Data>
            var failureCallbackWasCalled = false

            describe("when the response does NOT have errors") {
                beforeEach {
                    mockUpdateProductSerialNumbersInput = mockk()
                    mockProduct = mockk {
                        every { setStartPickTime(any()) } returns Unit
                        every { setTote() } returns Unit
                        every { getProductDTO() } returns mockk()
                    }
                    mockResponse = mockk {
                        every { errors } returns null
                    }
                    mockFailureCallback = { failureCallbackWasCalled = true }
                    coEvery { mockRepository.mutationUpdateProductSerialNumbers(any()) } returns mockResponse
                    uut.updateProductSerialNumbers(
                        mockUpdateProductSerialNumbersInput,
                        mockProduct,
                        mockFailureCallback
                    )
                }

                describe("when .submitPickItem()") {
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
                }
            }

            describe("when the response has errors") {
                lateinit var mockError: Error

                beforeEach {
                    every { mockUserPreferences.setErrorLog(allAny()) } returns Unit
                    mockFailureCallback = { failureCallbackWasCalled = true }

                    mockError = mockk {
                        every { message } returns "someErrorMessage"
                    }
                    mockResponse = mockk {
                        every { errors } returns null
                        every { errors } returns listOf(mockError)
                    }

                    coEvery { mockRepository.mutationUpdateProductSerialNumbers(any()) } returns mockResponse

                    every { mockActivityService.errorMessage(any()) } returns Unit

                    uut.selectedOrder.value = mockk()

                    uut.updateProductSerialNumbers(
                        mockk(),
                        mockk(),
                        mockFailureCallback
                    )

                }
            }
        }

        describe("when .submitPickItem()") {

            lateinit var mockProduct: ProductModel
            lateinit var mockFailureCallback: () -> Unit
            var failureCallbackWasCalled = false
            lateinit var mockProductDTO: ProductDTO

            beforeEach {
                mockProductDTO = mockk()

                mockProduct = mockk {
                    every { setStartPickTime(any()) } returns Unit
                    every { setTote() } returns Unit
                    every { getProductDTO() } returns mockProductDTO
                }

                mockFailureCallback = { failureCallbackWasCalled = true }

                uut.submitPickItem(
                    mockProduct,
                    mockFailureCallback
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

                        mockFailureCallback = mockk()

                        uut.loaderState.value = LoaderState.Default

                        uut.submitPickItem(
                            mockProduct,
                            mockFailureCallback
                        )
                    }

                    describe("when .queryPickTasks()") {
                        describe("when .selectedOrder has a value") {
                            describe("when .showQueryPickTaskLoading is false") {
                                it("does not change the value of .loaderState") {
                                    eventually(duration = delayDuration) {
                                        uut.loaderState.value shouldBe LoaderState.Default
                                    }
                                }
                            }
                        }
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
                            mockProduct,
                            mockFailureCallback
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

        describe("when .splitQuantityForProduct()") {
            lateinit var mockProduct: ProductModel
            lateinit var mockUpdateProductSerialNumbersInput: UpdateProductSerialNumbersInput
            var quantityPicked = 0
            lateinit var mockFailureCallback: () -> Unit
            var failureCallbackWasCalled = false
            lateinit var mockResponse: Response<SplitQuantityMutation.Data>

            describe("when the response does NOT have errors") {
                beforeEach {
                    mockUpdateProductSerialNumbersInput = mockk()
                    quantityPicked = mockk()
                    mockProduct = mockk {
                        every { setStartPickTime(any()) } returns Unit
                        every { setTote() } returns Unit
                        every { getProductDTO() } returns mockk()
                    }
                    mockResponse = mockk {
                        every { errors } returns null
                    }
                    mockFailureCallback = { failureCallbackWasCalled = true }
                    coEvery { mockRepository.mutationSplitQuantity(any()) } returns mockResponse
                    uut.splitQuantityForProduct(
                        mockProduct,
                        mockUpdateProductSerialNumbersInput,
                        quantityPicked,
                        mockFailureCallback
                    )
                }
            }

        }

        describe("when .splitQuantityForProduct()") {
            lateinit var mockProduct: ProductModel
            lateinit var mockUpdateProductSerialNumbersInput: UpdateProductSerialNumbersInput
            var quantityPicked = 0
            lateinit var mockFailureCallback: () -> Unit
            var failureCallbackWasCalled = false
            lateinit var mockResponse: Response<SplitQuantityMutation.Data>

            describe("when the response does NOT have errors") {
                beforeEach {
                    mockUpdateProductSerialNumbersInput = mockk()
                    quantityPicked = mockk()
                    mockProduct = mockk {
                        every { setStartPickTime(any()) } returns Unit
                        every { setTote() } returns Unit
                        every { getProductDTO() } returns mockk()
                    }
                    mockResponse = mockk {
                        every { errors } returns null
                    }
                    mockFailureCallback = { failureCallbackWasCalled = true }
                    coEvery { mockRepository.mutationSplitQuantity(any()) } returns mockResponse
                    uut.splitQuantityForProduct(
                        mockProduct,
                        mockUpdateProductSerialNumbersInput,
                        quantityPicked,
                        mockFailureCallback
                    )
                }
            }

        }

          //TODO : Later uncomment below code
//        describe("when .stagePickingTasks()") {
//            lateinit var mockResponse: Response<StagePickTasksMutation.Data>
//            lateinit var mockOrderViewModel: OrderViewModel
//
//            describe("when the response has NO errors") {
//                beforeEach {
//                    mockOrderViewModel = mockk {
//                        every { orderId } returns "someOrderId"
//                        every { invoiceId } returns "someInvoiceId"
//                        every { invoiceId } returns "someInvoiceId"
//                        every { getToteName() } returns "someToteName"
//                    }
//                    uut.selectedOrder.value = mockOrderViewModel
//
//                    every { mockUserPreferences.getBranch() } returns "someBranchId"
//
//                    mockResponse = mockk {
//                        every { errors } returns null
//                    }
//
//                    coEvery { mockRepository.mutationStagePickTasks(any()) } returns mockResponse
//
//                    every { mockActivityService.showMessage(any(), any()) } returns Unit
//
//                    uut.stagePickingTasks()
//                }
//
//                it("sets .loaderState to LoaderState.Loading") {
//                    eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                        uut.loaderState.value shouldBe LoaderState.Loading
//                    }
//                }
//
//                describe("when .stagePickTotePackages()") {
//                    lateinit var mockResponse: Response<StagePickTotePackagesMutation.Data>
//                    lateinit var mockCloseResponse: Response<CloseTaskMutation.Data>
//                    lateinit var mockOrderViewModel: OrderViewModel
//                    lateinit var mockError: Error
//
//                    describe("when the response has NO errors") {
//                        describe("when .closeTask()") {
//                            describe("when the response has NO errors") {
//                                beforeEach {
//                                    mockOrderViewModel = mockk {
//                                        every { orderId } returns "someOrderId"
//                                        every { invoiceId } returns "someInvoiceId"
//                                        every { invoiceId } returns "someInvoiceId"
//                                        every { getToteName() } returns "someToteName"
//                                    }
//                                    uut.selectedOrder.value = mockOrderViewModel
//
//                                    every { mockUserPreferences.getBranch() } returns "someBranchId"
//
//                                    mockResponse = mockk {
//                                        every { errors } returns null
//                                    }
//
//                                    mockCloseResponse = mockk {
//                                        every { errors } returns null
//                                    }
//
//                                    coEvery { mockRepository.mutationPostPackageData(any()) } returns mockResponse
//                                    coEvery { mockRepository.mutationCloseTask(any()) } returns mockCloseResponse
//
//                                    every {
//                                        mockActivityService.showMessage(
//                                            any(),
//                                            any()
//                                        )
//                                    } returns Unit
//
//                                    mockNavigateAfterStagingCallback =
//                                        { navigateAfterStagingCallbackWasCalled = true }
//                                    uut.navigateAfterStagingCallback =
//                                        mockNavigateAfterStagingCallback
//
//                                    uut.stagePickingTasks()
//                                }
//
//                                describe("when .postAlertAndNavigateBack()") {
//                                    it("calls .activityService.showMessage") {
//                                        eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                                            verify {
//                                                mockActivityService.showMessage(
//                                                    any(),
//                                                    Snackbar.LENGTH_SHORT
//                                                )
//                                            }
//                                        }
//                                    }
//
//                                    it("calls .navigateAfterStagingCallback") {
//                                        navigateAfterStagingCallbackWasCalled shouldBe true
//                                    }
//                                }
//                            }
//
//                            describe("when the response has errors") {
//                                beforeEach {
//                                    mockOrderViewModel = mockk {
//                                        every { orderId } returns "someOrderId"
//                                        every { invoiceId } returns "someInvoiceId"
//                                        every { invoiceId } returns "someInvoiceId"
//                                        every { getToteName() } returns "someToteName"
//                                    }
//                                    uut.selectedOrder.value = mockOrderViewModel
//
//                                    every { mockUserPreferences.getBranch() } returns "someBranchId"
//
//                                    mockResponse = mockk {
//                                        every { errors } returns null
//                                    }
//
//                                    mockError = mockk {
//                                        every { message } returns "someErrorMessage"
//                                    }
//                                    mockResponse = mockk {
//                                        every { errors } returns listOf(mockError)
//                                    }
//
//                                    coEvery { mockRepository.mutationPostPackageData(any()) } returns mockResponse
//                                    coEvery { mockRepository.mutationCloseTask(any()) } returns mockCloseResponse
//
//                                    every {
//                                        mockActivityService.showMessage(
//                                            any(),
//                                            any()
//                                        )
//                                    } returns Unit
//
//                                    mockNavigateAfterStagingCallback =
//                                        { navigateAfterStagingCallbackWasCalled = true }
//                                    uut.navigateAfterStagingCallback =
//                                        mockNavigateAfterStagingCallback
//
//                                    uut.stagePickingTasks()
//                                }
//
//                                describe("when .setProductsResponseState()") {
//                                    it("sets .setStageOrderResponseState to ResponseState.Error()") {
//                                        eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                                            uut.stageOrderResponseState.value!!::class shouldBe ResponseState.Error::class
//                                        }
//                                    }
//
//                                    describe("that Error response state") {
//                                        it("has a message of 'someErrorMessage'") {
//                                            eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
//                                                uut.stageOrderResponseState.value?.message shouldBe "someErrorMessage"
//                                            }
//                                        }
//                                    }
//                                }
//
//                                it("calls .activityService.activity.errorMessage()") {
//                                    eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                                        verify { mockActivityService.errorMessage("someErrorMessage") }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    describe("when the response has errors") {
//
//                        lateinit var mockError: Error
//
//                        beforeEach {
//                            mockOrderViewModel = mockk {
//                                every { orderId } returns "someOrderId"
//                                every { invoiceId } returns "someInvoiceId"
//                                every { invoiceId } returns "someInvoiceId"
//                                every { getToteName() } returns "someToteName"
//                            }
//                            uut.selectedOrder.value = mockOrderViewModel
//
//                            every { mockUserPreferences.getBranch() } returns "someBranchId"
//
//                            mockError = mockk {
//                                every { message } returns "someErrorMessage"
//                            }
//                            mockResponse = mockk {
//                                every { errors } returns listOf(mockError)
//                            }
//                            coEvery { mockRepository.mutationPostPackageData(any()) } returns mockResponse
//                            uut.stagePickingTasks()
//                        }
//
//                        describe("when .setProductsResponseState()") {
//                            it("sets .setStageOrderResponseState to ResponseState.Error()") {
//                                eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                                    uut.stageOrderResponseState.value!!::class shouldBe ResponseState.Error::class
//                                }
//                            }
//
//                            describe("that Error response state") {
//                                it("has a message of 'someErrorMessage'") {
//                                    eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
//                                        uut.stageOrderResponseState.value?.message shouldBe "someErrorMessage"
//                                    }
//                                }
//                            }
//                        }
//
//                        it("calls .activityService.activity.errorMessage()") {
//                            eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                                verify { mockActivityService.errorMessage("someErrorMessage") }
//                            }
//                        }
//                    }
//                }
//            }
//
//            describe("when the response has errors") {
//
//                lateinit var mockError: Error
//
//                beforeEach {
//                    mockOrderViewModel = mockk {
//                        every { orderId } returns "someOrderId"
//                        every { invoiceId } returns "someInvoiceId"
//                        every { invoiceId } returns "someInvoiceId"
//                        every { getToteName() } returns "someToteName"
//                    }
//                    uut.selectedOrder.value = mockOrderViewModel
//
//                    every { mockUserPreferences.getBranch() } returns "someBranchId"
//
//                    mockError = mockk {
//                        every { message } returns "someErrorMessage"
//                    }
//                    mockResponse = mockk {
//                        every { errors } returns listOf(mockError)
//                    }
//                    coEvery { mockRepository.mutationStagePickTasks(any()) } returns mockResponse
//
//                    uut.locationDataEntryViewModel.value = mockLocationDataEntryViewModel
//                    uut.boxesDataEntryViewModel.value = mockBoxesDataEntryViewModel
//                    uut.skidsDataEntryViewModel.value = mockSkidsDataEntryViewModel
//                    uut.bundlesDataEntryViewModel.value = mockBundlesDataEntryViewModel
//                    uut.piecesDataEntryViewModel.value = mockPiecesDataEntryViewModel
//
//                    uut.stagePickingTasks()
//                }
//
//                describe("when .setStageOrderResponseState()") {
//                    it("sets .setStageOrderResponseState to ResponseState.Error()") {
//                        eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                            uut.stageOrderResponseState.value!!::class shouldBe ResponseState.Error::class
//                        }
//                    }
//
//                    describe("that Error response state") {
//                        it("has a message of 'someErrorMessage'") {
//                            eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
//                                uut.stageOrderResponseState.value?.message shouldBe "someErrorMessage"
//                            }
//                        }
//                    }
//                }
//
//                describe("when .setLoaderState()") {
//                    it("sets .loaderState to LoaderState.Default") {
//                        eventually(duration = delayDuration) {
//                            uut.loaderState.value shouldBe LoaderState.Default
//                        }
//                    }
//                }
//
//                describe("when .setDataEntryInputState()") {
//                    it("calls .locationDataEntryViewModel.setInputState() passing InputState.Default()") {
//                        eventually(duration = delayDuration) {
//                            verify {
//                                mockLocationDataEntryViewModel.setInputState(any())
//                            }
//                        }
//                    }

//                    it("calls .boxesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                        eventually(duration = delayDuration) {
//                            verify {
//                                mockBoxesDataEntryViewModel.setInputState(any())
//                            }
//                        }
//                    }
//                    it("calls .skidsDataEntryViewModel.setInputState() passing InputState.Default()") {
//                        eventually(duration = delayDuration) {
//                            verify {
//                                mockSkidsDataEntryViewModel.setInputState(any())
//                            }
//                        }
//                    }
//                    it("calls .bundlesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                        eventually(duration = delayDuration) {
//                            verify {
//                                mockBundlesDataEntryViewModel.setInputState(any())
//                            }
//                        }
//                    }
//                    it("calls .piecesDataEntryViewModel.setInputState() passing InputState.Default()") {
//                        eventually(duration = delayDuration) {
//                            verify {
//                                mockPiecesDataEntryViewModel.setInputState(any())
//                            }
//                        }
//                    }
//                }
//
//                it("calls .activityService.errorMessage()") {
//                    eventually(duration = 5.toDuration(DurationUnit.SECONDS)) {
//                        verify { mockActivityService.errorMessage("someErrorMessage") }
//                    }
//                }
//            }
//        }

        describe("when .getOrderText()") {

            lateinit var mockOrderViewModel: OrderViewModel

            describe("when .selectedOrder.getOrderText() has value") {

                var response: Spanned? = null

                beforeEach {
                    mockOrderViewModel = mockk {
                        every { getOrderText() } returns "someOrderId.someInvoiceId"
                    }
                    uut.selectedOrder.value = mockOrderViewModel

                    response = uut.getOrderText()
                }

                it("calls .stringConverter.convertToHtmlSpan()") {
                    verify { mockStringConverter.convertToHtmlSpan(any()) }
                }

                it("returns a Spanned string") {
                    response shouldNotBe null
                }
            }

            describe("when .selectedOrder.getOrderText() has no value") {
                var response: Spanned? = null

                beforeEach {
                    uut.selectedOrder.value = null

                    response = uut.getOrderText()
                }

                it("returns null") {
                    response shouldBe null
                }
            }
        }

        describe("when .getCustomerText()") {

            lateinit var mockOrderViewModel: OrderViewModel

            describe("when .selectedOrder.shipToName has value") {

                var response: Spanned? = null

                beforeEach {
                    mockOrderViewModel = mockk {
                        every { shipToName } returns "someShipToName"
                    }
                    uut.selectedOrder.value = mockOrderViewModel

                    response = uut.getCustomerText()
                }

                it("calls .stringConverter.convertToHtmlSpan()") {
                    verify { mockStringConverter.convertToHtmlSpan(any()) }
                }

                it("returns a Spanned string") {
                    response shouldNotBe null
                }
            }

            describe("when .selectedOrder.shipToName has no value") {
                var response: Spanned? = null

                beforeEach {
                    uut.selectedOrder.value = null

                    response = uut.getCustomerText()
                }

                it("returns null") {
                    response shouldBe null
                }
            }
        }

        describe("when .getShipViaText()") {

            lateinit var mockOrderViewModel: OrderViewModel

            describe("when .selectedOrder.shipVia has value") {

                var response: Spanned? = null

                beforeEach {
                    mockOrderViewModel = mockk {
                        every { shipVia } returns "someShipVia"
                    }
                    uut.selectedOrder.value = mockOrderViewModel

                    response = uut.getShipViaText()
                }

                it("calls .stringConverter.convertToHtmlSpan()") {
                    verify { mockStringConverter.convertToHtmlSpan(any()) }
                }

                it("returns a Spanned string") {
                    response shouldNotBe null
                }
            }

            describe("when .selectedOrder.shipVia has no value") {
                var response: Spanned? = null

                beforeEach {
                    uut.selectedOrder.value = null

                    response = uut.getShipViaText()
                }

                it("returns null") {
                    response shouldBe null
                }
            }
        }

        describe("when .getPickCount()") {

            lateinit var mockOrderViewModel: OrderViewModel

            describe("when .selectedOrder.order.pickCount has value") {

                var response: Spanned? = null

                beforeEach {
                    mockOrderViewModel = mockk {
                        every { order.pickCount } returns "somePickCount"
                    }
                    uut.selectedOrder.value = mockOrderViewModel

                    response = uut.getPickCount()
                }

                it("calls .stringConverter.convertToHtmlSpan()") {
                    verify { mockStringConverter.convertToHtmlSpan(any()) }
                }

                it("returns a Spanned string") {
                    response shouldNotBe null
                }
            }

            describe("when .selectedOrder.order.pickCount has no value") {
                var response: Spanned? = null

                beforeEach {
                    uut.selectedOrder.value = null

                    response = uut.getPickCount()
                }

                it("returns null") {
                    response shouldBe null
                }
            }
        }

        describe("when .onEditorAction()") {
            describe("when the action is on the last entry field") {

                lateinit var mockTextView: TextView
                lateinit var mockKeyEvent: KeyEvent

                beforeEach {
                    mockTextView = mockk {
                        every { id } returns 1234
                    }
                    mockKeyEvent = mockk()
                    mockPiecesDataEntryViewModel = mockk {
                        every {
                            getInputString()
                        } returns "some value"
                        every {
                            setInputState(any())
                        } returns Unit
                        every {
                            getTextInputId()
                        } returns mockTextView.hashCode()
                    }
                    uut.onEditorAction(mockTextView, 0, mockk())
                    uut.piecesDataEntryViewModel.value = mockPiecesDataEntryViewModel

                    it("calls .activityService.hideKeyboard()") {
                        verify { mockActivityService.hideKeyboard() }
                    }
                }
            }
        }

        describe("when .afterTextChange()") {

            describe("when .getLocationValue() is not empty") {

                beforeEach {
                    mockLocationDataEntryViewModel = mockk {
                        every {
                            getInputString()
                        } returns "some value"
                        every {
                            setInputState(any())
                        } returns Unit
                        every {
                            getTextInputId()
                        } returns 1234
                    }
                    uut.locationDataEntryViewModel.value = mockLocationDataEntryViewModel
                    uut.afterTextChanged(1234, mockk())
                }

                describe("when the passed id is same as .locationDataEntryViewModel.getTextInputId()") {
                    describe("when .setStagingOrderState()") {
                        it("sets stagingState to StageReady") {
                            eventually(duration = delayDuration) {
                                uut.stagingState.value shouldBe StagingOrderState.StageReady
                            }
                        }
                    }
                    describe("when .setLocationDataEntryInputState()") {
                        it("calls .locationDataEntryViewModel.setInputState() passing InputState.Default()") {
                            eventually(duration = delayDuration) {
                                verify {
                                    mockLocationDataEntryViewModel.setInputState(withArg { state ->
                                        state::class shouldBe InputState.Default::class
                                    })
                                }
                            }
                        }
                    }
                }
            }

            describe("when .getLocationValue() is empty") {

                beforeEach {
                    mockLocationDataEntryViewModel = mockk {
                        every {
                            getInputString()
                        } returns null
                        every {
                            setInputState(any())
                        } returns Unit
                        every {
                            getTextInputId()
                        } returns 1234
                    }
                    uut.locationDataEntryViewModel.value = mockLocationDataEntryViewModel
                    uut.afterTextChanged(1234, mockk())
                }
                describe("when the passed id is same as .locationDataEntryViewModel.getTextInputId()") {
                    describe("when .setStagingOrderState()") {
                        it("sets stagingState to Default") {
                            eventually(duration = delayDuration) {
                                uut.stagingState.value shouldBe StagingOrderState.Default
                            }
                        }
                    }
                    describe("when .setLocationDataEntryInputState()") {
                        it("calls .locationDataEntryViewModel.setInputState() passing InputState.Error()") {
                            eventually(duration = delayDuration) {
                                verify {
                                    mockLocationDataEntryViewModel.setInputState(withArg { state ->
                                        state::class shouldBe InputState.Error::class
                                        state.message shouldBe "Field cannot be blank."
                                    })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
})
