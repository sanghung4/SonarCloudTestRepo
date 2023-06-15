package com.reece.pickingapp.viewmodel

import android.app.Activity
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.reece.pickingapp.AssignPickTaskMutation
import com.reece.pickingapp.PickingOrdersQuery
import com.reece.pickingapp.R
import com.reece.pickingapp.ShippingDetailsQuery
import com.reece.pickingapp.models.EclipseCredentialModel
import com.reece.pickingapp.models.ErrorLogDTO
import com.reece.pickingapp.models.OrderDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.EclipseLoginRepository
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.type.PickingTaskInput
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.adapter.OrdersAdapter
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import com.reece.pickingapp.wrappers.MaterialAlertDialogBuilderWrapper
import com.reece.pickingapp.wrappers.OktaWebAuthWrapper
import com.reece.pickingapp.wrappers.OrdersAdapterWrapper
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class OrdersListFragmentViewModelTest : DescribeSpec({
    describe("OrdersListFragmentViewModel") {
        lateinit var uut: OrdersListFragmentViewModel
        lateinit var mockPickingApi: PickingApi
        lateinit var mockRepository: PickingRepositoryImpl
        lateinit var mockUserPreferences: UserPreferencesImp
        lateinit var mockActivityService: ActivityService
        lateinit var mockStringConverter: StringConverter
        lateinit var mockOrdersAdapterWrapper: OrdersAdapterWrapper
        lateinit var mockOrdersAdapter: OrdersAdapter
        lateinit var mockMaterialAlertDialogBuilderWrapper: MaterialAlertDialogBuilderWrapper
        lateinit var mainThreadSurrogate: ExecutorCoroutineDispatcher
        lateinit var mockNavigationForAuthScreenCallback: () -> Unit
        var navigationForAuthScreenCallbackWasCalled = false
        lateinit var mockNavigationForOrderCallback: (OrderDTO) -> Unit
        var navigationForOrderCallbackWasCalled = false
        lateinit var mockResponse: Response<ShippingDetailsQuery.Data>
        lateinit var mockResponseOrderQuery: Response<PickingOrdersQuery.Data>
        lateinit var mockData: ShippingDetailsQuery.Data
        lateinit var mockOrderData: PickingOrdersQuery.Data
        lateinit var mockErrorData:  ArrayList<ErrorLogDTO>
        lateinit var mockOktaWebAuthWrapper: OktaWebAuthWrapper
        lateinit var mockRoomRepository: EclipseLoginRepository
        lateinit var mockEclipseCredentialModel: EclipseCredentialModel

        beforeEach {
            mainThreadSurrogate = newSingleThreadContext("UI thread")
            Dispatchers.setMain(mainThreadSurrogate)

            mockPickingApi = mockk{
                coEvery { getOktaUserEmail() } returns "someEmail@reece.com"

            }
            mockRepository = mockk()
            mockEclipseCredentialModel = mockk{
                every { eclipsePass  } returns "somePassword"
            }
            mockRoomRepository = mockk{
                coEvery { getCredentialByEmail(any()) } returns mockEclipseCredentialModel
                coEvery { getCountCredentials() } returns 0
            }
            mockUserPreferences = mockk {
                every { getBranch() } returns "someBranchId"
                every { getUsername() } returns "someUserName"
                every { getErrorLogs() } returns ArrayList<ErrorLogDTO>()
            }


            mockOktaWebAuthWrapper = mockk {
                coEvery { getToken() } returns "someToken"
                coEvery { getOktaUserEmail() } returns  "someEmail@reece.com"
                coEvery { refreshOktaToken() } returns Unit
                coEvery { refreshOktaToken(any(), any()) } returns Unit
            }
            mockErrorData=mockUserPreferences.getErrorLogs()

            mockActivityService = MockActivityService().create()

            mockStringConverter = mockk {
                every { convertToHtmlSpan(any()) } returns mockk()
            }
            mockOrdersAdapter = mockk()
            mockOrdersAdapterWrapper = mockk {
                every { createAdapter() } returns mockOrdersAdapter
            }

            mockMaterialAlertDialogBuilderWrapper = mockk()

            mockData = mockk()
            mockResponse = mockk {
                every { errors } returns null
                every { data } returns mockData
            }
            coEvery { mockRepository.queryShippingDetails(any()) } returns mockResponse

            mockOrderData = mockk()
            mockResponseOrderQuery = mockk {
                every { errors } returns null
                every { data } returns mockOrderData
            }
            coEvery { mockRepository.queryPickingTasks(any(),any()) } returns mockResponseOrderQuery

            uut = OrdersListFragmentViewModel(
                mockRepository,
                mockPickingApi,
                mockUserPreferences,
                mockActivityService,
                mockStringConverter,
                mockOrdersAdapterWrapper,
                mockMaterialAlertDialogBuilderWrapper,
                mockRoomRepository
            )
        }

        afterEach {
            Dispatchers.resetMain()
            mainThreadSurrogate.close()
        }

        describe("when .setUp()") {

            beforeEach {
                mockNavigationForAuthScreenCallback = { navigationForAuthScreenCallbackWasCalled = true }
                mockNavigationForOrderCallback = { order -> }
                uut.loaderState.value = LoaderState.Default
                uut.ordersResponseState.value = ResponseState.Cancelled()
                coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns true
                coEvery {mockOktaWebAuthWrapper.isAuthenticated() } returns true
                coEvery { mockPickingApi.isAuthenticated() } returns true
                coEvery { mockPickingApi.isOktaAuthenticated() } returns true
                uut.setUp(
                    navigationForAuthScreenCallback = mockNavigationForAuthScreenCallback,
                    navigationForOrderCallback = mockNavigationForOrderCallback
                )
            }

            it("sets .navigateToAuthScreenCallback") {
                uut.navigateToAuthScreenCallback shouldBe mockNavigationForAuthScreenCallback
            }

            it("sets .navigateToOrderCallback") {
                uut.navigateToOrderCallback shouldBe mockNavigationForOrderCallback
            }

            /*describe("when .queryOrdersList()") {
                describe("when .setLoaderState()") {
                    it("sets .loaderState to Default") {
                        eventually(duration = 50.toDuration(DurationUnit.SECONDS)) {
                            uut.loaderState.value shouldBe LoaderState.Default
                        }
                    }
                }



            }*/

            it("sets .ordersAdapter") {
                uut.ordersAdapter.value shouldBe mockOrdersAdapter
            }

            describe("when .isAuthenticated() is false") {
                beforeEach {
                    mockNavigationForAuthScreenCallback = { navigationForAuthScreenCallbackWasCalled = true }
                    mockNavigationForOrderCallback = { order -> }
                    coEvery { mockOktaWebAuthWrapper.isAuthenticated() } returns false
                    coEvery { mockPickingApi.isOktaAuthenticated() } returns false
                    coEvery { mockPickingApi.isAuthenticated() } returns false
                    uut.loaderState.value = LoaderState.Default
                    uut.ordersResponseState.value = ResponseState.Cancelled()

                    uut.setUp(
                        navigationForAuthScreenCallback = mockNavigationForAuthScreenCallback,
                        navigationForOrderCallback = mockNavigationForOrderCallback
                    )
                }

/*
                describe("when .navigateToAuthScreen()") {
                    it("calls .activityService.errorMessage()") {
                        verify { mockActivityService.errorMessage("Authorization expired. Please log in again.") }
                    }
                }
*/

                /*describe("when currentEclipseCredential is null"){
                    beforeEach {
                        coEvery { mockRoomRepository.getCredentialByEmail(any()) } returns null
                    }
                    it("calls .activityService.errorMessage()"){
                        verify { mockActivityService.errorMessage(any()) }
                    }
                }*/
            }
        }

        describe(".onItemClicked") {
            describe("when called") {
                describe("when .orderViewModel.assignedUserId equals getUsername()") {

                    lateinit var mockOrderDTO: OrderDTO

                    beforeEach {
                        mockOrderDTO = mockk {
                            every { orderId } returns "someOrderId"
                            every { shipToName } returns "someShipToName"
                            every { branchId } returns "someBranchId"
                            every { invoiceId } returns "someInvoiceId"
                            every { shipVia } returns "someShipVia"
                            every { assignedUserId } returns "someUserName"
                        }
                        val orderViewModel = OrderViewModel(mockOrderDTO)
                        every { mockUserPreferences.getUsername() } returns "someUserName"
                        mockNavigationForOrderCallback = { order -> navigationForOrderCallbackWasCalled = true}
                        uut.navigateToOrderCallback = mockNavigationForOrderCallback

                        uut.onItemClicked(orderViewModel)
                    }

                    it("calls .navigateToOrderCallback()") {
                        navigationForOrderCallbackWasCalled shouldBe true
                    }
                }

                describe("when .orderViewModel.assignedUserId does not equal getUsername()") {

                    lateinit var mockOrderDTO: OrderDTO
                    lateinit var mockMaterialAlertDialogBuilder:
                            MaterialAlertDialogBuilder
                    lateinit var mockActivity: Activity
                    lateinit var mockAlertDialog: androidx.appcompat.app.AlertDialog

                    beforeEach {
                        mockOrderDTO = mockk {
                            every { assignedUserId } returns "notEqualingName"
                            every { orderId } returns "someOrderId"
                            every { shipToName } returns "someShipToName"
                            every { branchId } returns "someBranchId"
                            every { invoiceId } returns "someInvoiceId"
                            every { shipVia } returns "someShipVia"
                        }
                        mockAlertDialog = mockk()
                        mockMaterialAlertDialogBuilder = mockk()
                        every { mockMaterialAlertDialogBuilder.setTitle("Are you ready to start this order?") } returns mockMaterialAlertDialogBuilder
                        every { mockMaterialAlertDialogBuilder.setMessage(mockStringConverter.convertToHtmlSpan("someShipToName\n\nOrder Number: someOrderId")) } returns mockMaterialAlertDialogBuilder
                        every {
                            mockMaterialAlertDialogBuilder.setNegativeButton(
                                "Cancel",
                                any()
                            )
                        } returns mockMaterialAlertDialogBuilder
                        every {
                            mockMaterialAlertDialogBuilder.setPositiveButton(
                                "READY TO START",
                                any()
                            )
                        } returns mockMaterialAlertDialogBuilder
                        every { mockMaterialAlertDialogBuilder.show() } returns mockAlertDialog

                        every {
                            mockMaterialAlertDialogBuilderWrapper.create(
                                any(),
                                any()
                            )
                        } returns mockMaterialAlertDialogBuilder

                        val orderViewModel = OrderViewModel(mockOrderDTO)

                        mockActivity = mockk {

                        }
                        every { mockActivityService.activity } returns mockActivity
                        every {
                            mockActivityService.getString(
                                R.string.order_dialog_info,
                                "someShipToName",
                                "someOrderId"
                            )
                        } returns "someShipToName\n\nOrder Number: someOrderId"
                        every { mockUserPreferences.getUsername() } returns "someUserName"
                        uut.onItemClicked(orderViewModel)
                    }

                    it("calls .materialAlertDialogBuilderWrapper.create") {
                        verify {
                            mockMaterialAlertDialogBuilderWrapper.create(
                                mockActivity,
                                R.style.MorscoConfirmationStyle
                            )
                        }
                    }

                    describe("that MaterialAlertDialogBuilder") {
                        it("calls .setTitle()") {
                            verify { mockMaterialAlertDialogBuilder.setTitle("Are you ready to start this order?") }
                        }
                        it("calls .setMessage()") {
                            verify { mockMaterialAlertDialogBuilder.setMessage(mockStringConverter.convertToHtmlSpan("someShipToName\n\nOrder Number: someOrderId")) }
                        }
                        it("calls .setNegativeButton()") {
                            verify {
                                mockMaterialAlertDialogBuilder.setNegativeButton(
                                    "Cancel",
                                    any()
                                )
                            }
                        }
                        it("calls .setPositiveButton()") {
                            verify {
                                mockMaterialAlertDialogBuilder.setPositiveButton(
                                    "READY TO START",
                                    any()
                                )
                            }
                        }
                        it("calls .show()") {
                            verify { mockMaterialAlertDialogBuilder.show() }
                        }
                    }
                }
            }
        }

        describe("when .queryOrdersList()") {
            lateinit var mockResponse: Response<PickingOrdersQuery.Data>

            beforeEach {
                coEvery { mockRepository.queryPickingTasks(any(), any()) } returns null
                uut.queryOrdersList()
            }

            describe("when .setLoaderState()") {
                it("sets .loaderState to Loading") {
                    eventually(duration = delayDuration) {
                        uut.loaderState.value shouldBe LoaderState.Loading
                    }
                }
            }

            describe("when .setOrdersResponseState()") {
                it("sets .ordersResponseState to Default") {
                    eventually(duration = delayDuration) {
                        uut.ordersResponseState.value!!::class shouldBe ResponseState.Default::class
                    }
                }
            }

            describe("when the response does NOT have errors") {
                lateinit var mockData: PickingOrdersQuery.Data
                lateinit var mockPickingOrder: PickingOrdersQuery.PickingOrder
                lateinit var mockPickingOrders: List<PickingOrdersQuery.PickingOrder?>

                beforeEach {
                    mockData = mockk()
                    mockResponse = mockk {
                        every { errors } returns null
                        every { data } returns mockData
                    }
                    coEvery { mockRepository.queryPickingTasks(any(), any()) } returns mockResponse
                    uut.queryOrdersList()
                }

                describe("when .setLoaderState()") {
                    it("sets .loaderState to Default") {
                        eventually(duration = delayDuration) {
                            uut.loaderState.value shouldBe LoaderState.Default
                        }
                    }
                }

                describe("when .setOrdersResponseState()") {

                    it("sets .ordersResponseState to Success") {
                        eventually(duration = delayDuration) {
                            uut.ordersResponseState.value!!::class shouldBe ResponseState.Success::class
                        }
                    }

                    it("sets .ordersResponseState.response to the call's response") {
                        eventually(duration = delayDuration) {
                            uut.ordersResponseState.value?.response shouldBe mockResponse
                        }
                    }

                    describe("when the response has orders") {
                        beforeEach {
                            mockPickingOrder = PickingOrdersQuery.PickingOrder(
                                orderId = "someOrderId",
                                generationId = 1,
                                invoiceId = "someInvoiceId",
                                branchId = "someBranchId",
                                pickGroup = "somePickGroup",
                                assignedUserId = null,
                                billTo = 0,
                                shipTo = 0,
                                shipToName = "somePickToName",
                                pickCount = "somePickCount",
                                shipVia = "someShipVia",
                                isFromMultipleZones = false,
                                taskState = "someTaskState",
                                taskWeight = 1.0
                            )

                            mockPickingOrders = listOf(mockPickingOrder)
                            mockData = mockk {
                                every { pickingOrders } returns mockPickingOrders
                            }
                            mockResponse = mockk {
                                every { errors } returns null
                                every { data } returns mockData
                            }

                            coEvery {
                                mockRepository.queryPickingTasks(
                                    any(),
                                    any()
                                )
                            } returns mockResponse

                            uut.ordersAdapter.value = mockOrdersAdapter
                            uut.queryOrdersList()
                        }

                        describe("when .generateViewHolder()") {
                            it("sets .orderViewHolderViewModels to the pickingOrders") {
                                eventually(duration = delayDuration) {
                                    uut.orderViewHolderViewModels.size shouldBe 1
                                }
                            }
                        }

                        it("calls .ordersAdapter.submitList() passing the .orderViewHolderViewModels") {
                            eventually(duration = delayDuration) {
                                verify { mockOrdersAdapter.submitList(uut.orderViewHolderViewModels) }
                            }
                        }
                    }


                    describe("when the response does not have orders") {
                        beforeEach {

                            mockPickingOrders = emptyList()
                            mockData = mockk {
                                every { pickingOrders } returns mockPickingOrders
                            }
                            mockResponse = mockk {
                                every { errors } returns null
                                every { data } returns mockData
                            }

                            coEvery {
                                mockRepository.queryPickingTasks(
                                    any(),
                                    any()
                                )
                            } returns mockResponse

                            uut.ordersAdapter.value = mockOrdersAdapter
                            uut.queryOrdersList()
                        }

                        it("calls .ordersAdapter.submitList() passing the an empty list") {
                            eventually(duration = delayDuration) {
                                verify { mockOrdersAdapter.submitList(uut.orderViewHolderViewModels) }
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
                    coEvery { mockRepository.queryPickingTasks(any(), any()) } returns mockResponse
                    uut.queryOrdersList()
                }

                describe("when .setLoaderState()") {
                    it("sets .loaderState to LoaderState.Default") {
                        eventually(duration = delayDuration) {
                            uut.loaderState.value shouldBe LoaderState.Default
                        }
                    }
                }

                describe("when .setProductsResponseState()") {
                    it("sets .ordersResponseState to ResponseState.Error()") {
                        eventually(duration = delayDuration) {
                            uut.ordersResponseState.value!!::class shouldBe ResponseState.Error::class
                        }
                    }

                    describe("that Error response state") {
                        it("has a message of 'someErrorMessage'") {
                            eventually(duration = delayDuration) {
                                uut.ordersResponseState.value?.message shouldBe "someErrorMessage"
                            }
                        }
                    }
                }
            }
        }

        describe("when .assignPickTask()") {

            lateinit var mockResponse: Response<AssignPickTaskMutation.Data>
            lateinit var mockOrderViewModel: OrderViewModel
            lateinit var mockOrderDTO: OrderDTO
            lateinit var mockPickingTaskInput: PickingTaskInput

            beforeEach {
                uut.assignPickTask(mockk())
            }

            describe("when .setLoaderState()") {
                it("sets .loaderState to Loading") {
                    eventually(duration = delayDuration) {
                        uut.loaderState.value shouldBe LoaderState.Loading
                    }
                }
            }

            describe("when .setOrdersResponseState()") {
                it("sets .ordersResponseState to Default") {
                    eventually(duration = delayDuration) {
                        uut.ordersResponseState.value!!::class shouldBe ResponseState.Default::class
                    }
                }
            }

            //TODO: weak ref testing
//            describe("when the response does NOT have errors") {
//                lateinit var mockData: AssignPickTaskMutation.Data
//                lateinit var mockPickTask: AssignPickTaskMutation.AssignPickTask
//
//                beforeEach {
//                    mockOrderDTO = mockk {
//                        every { toPickingTaskInput(any()) } returns mockPickingTaskInput
//                    }
//                    mockPickTask = mockk {
//                        every { toOrderDTO() } returns mockOrderDTO
//                    }
//                    mockData = mockk {
//                        every { assignPickTask } returns mockPickTask
//                    }
//                    mockResponse = mockk {
//                        every { errors } returns null
//                        every { data } returns mockData
//                    }
//                    mockPickingTaskInput = mockk()
//                    mockOrderViewModel = mockk {
//                        every { getOrderDTO() } returns mockOrderDTO
//                    }
//                    uut.assignPickTask(mockOrderViewModel)
//                }
//
//                it("calls") {
//                    verify {  }
//                }
//            }

            describe("when the response has errors") {
                lateinit var mockError: Error

                beforeEach {
                    mockPickingTaskInput = mockk()
                    mockOrderDTO = mockk {
                        every { toPickingTaskInput(any()) } returns mockPickingTaskInput
                    }
                    mockError = mockk {
                        every { message } returns "someErrorMessage"
                    }
                    mockResponse = mockk {
                        every { data } returns null
                        every { errors } returns listOf(mockError)
                    }
                    mockOrderViewModel = mockk {
                        every { getOrderDTO() } returns mockOrderDTO
                    }

                    every { mockActivityService.errorMessage(any()) } returns Unit
                    coEvery { mockRepository.mutationAssignPickTask(any()) } returns mockResponse
                    coEvery { mockRepository.queryPickingTasks(any(), any()) } returns null
                    uut.assignPickTask(mockOrderViewModel)
                }

                describe("when .setOrdersResponseState()") {
                    it("calls .activityService.errorMessage()") {
                        eventually(duration = delayDuration) {
                            verify { mockActivityService.errorMessage("someErrorMessage") }
                        }
                    }
                }

                describe("when .queryOrdersList()") {
                    describe("when .setLoaderState()") {
                        it("sets .loaderState to Loading") {
                            eventually(duration = delayDuration) {
                                uut.loaderState.value shouldBe LoaderState.Loading
                            }
                        }
                    }

                    describe("when .setOrdersResponseState()") {
                        it("sets .ordersResponseState to Default") {
                            eventually(duration = delayDuration) {
                                uut.ordersResponseState.value!!::class shouldBe ResponseState.Default::class
                            }
                        }
                    }
                }
            }
        }

        describe("when .filterResults()") {
            lateinit var mockOrderViewModel1: OrderViewModel
            lateinit var mockOrderViewModel2: OrderViewModel
            lateinit var mockOrderViewModel3: OrderViewModel

            beforeEach {
                mockOrderViewModel1 = mockk {
                    every { shipToName } returns "ABC"
                    every { orderId } returns "123"
                    every { shipVia } returns "Fed Ex"
                }
                mockOrderViewModel2 = mockk {
                    every { shipToName } returns "DEF"
                    every { orderId } returns "456"
                    every { shipVia } returns "Pick Up"
                }
                mockOrderViewModel3 = mockk {
                    every { shipToName } returns "GHI"
                    every { orderId } returns "789"
                    every { shipVia } returns "Will Call"
                }
                uut.orderViewHolderViewModels = listOf(
                    OrderViewHolderViewModel(mockk(), mockk(), mockOrderViewModel1, mockk(), mockRepository),
                    OrderViewHolderViewModel(mockk(), mockk(), mockOrderViewModel2, mockk(), mockRepository),
                    OrderViewHolderViewModel(mockk(), mockk(), mockOrderViewModel3, mockk(), mockRepository)
                )
                every { mockOrdersAdapter.submitList(any()) } returns Unit
                every { mockOrdersAdapter.submitList(any(), any()) } returns Unit
                every { mockOrdersAdapter.recyclerViewScrollToTop() } returns Unit
                every { mockOrdersAdapter.currentList } returns uut.orderViewHolderViewModels

                uut.ordersAdapter.value = mockOrdersAdapter
            }

            describe("when .hasFilterTerm() returns true for customer name (shipToName)") {
                beforeEach {
                    uut.filterResults("ABC")
                }

                it("calls .orderAdapter.submitList()") {
                    verify {
                        mockOrdersAdapter.submitList(
                            withArg {
                                it.first().orderViewModel shouldBe mockOrderViewModel1
                            },
                            any()
                        )
                    }
                }
            }

            describe("when orderId contains the filter term") {
                beforeEach {
                    uut.filterResults("456")
                }

                it("calls .orderAdapter.submitList()") {
                    verify {
                        mockOrdersAdapter.submitList(
                            withArg {
                                it.first().orderViewModel shouldBe mockOrderViewModel2
                            },
                            any()
                        )
                    }
                }
            }

            describe("when shipVia contains the filter term") {
                beforeEach {
                    uut.filterResults("Call")
                }

                it("calls .orderAdapter.submitList()") {
                    verify {
                        mockOrdersAdapter.submitList(
                            withArg {
                                it.first().orderViewModel shouldBe mockOrderViewModel3
                            },
                            any()
                        )
                    }
                }
            }

            describe("when no searchable item contains the filter term") {
                beforeEach {
                    uut.filterResults("no result")
                }

                it("calls .orderAdapter.submitList()") {
                    verify { mockOrdersAdapter.submitList(  withArg {
                        it shouldBe emptyList<OrderViewHolderViewModel>()
                    },
                        any()) }
                }
            }
        }
    }
})
