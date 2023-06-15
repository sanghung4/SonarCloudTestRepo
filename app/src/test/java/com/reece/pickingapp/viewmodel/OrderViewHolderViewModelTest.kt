package com.reece.pickingapp.viewmodel

import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.api.Response
import com.reece.pickingapp.PickImageQuery
import com.reece.pickingapp.ShippingDetailsQuery
import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.testingUtils.MockActivityService
import com.reece.pickingapp.testingUtils.delayDuration
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.SHIPPING_INSTRUCTION_MIN_LINE
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.state.LoaderState
import com.reece.pickingapp.view.state.ResponseState
import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class OrderViewHolderViewModelTest : DescribeSpec({
    describe("OrderViewHolderViewModel") {
        lateinit var mockRepository: PickingRepositoryImpl
        lateinit var mockResponse: Response<ShippingDetailsQuery.Data>
        lateinit var mockData: ShippingDetailsQuery.Data
        lateinit var uut: OrderViewHolderViewModel
        lateinit var mockActivityService: ActivityService
        lateinit var mockStringConverter: StringConverter
        lateinit var mockOrderViewModel: OrderViewModel
        lateinit var mockOnClicked: ((orderViewModel: OrderViewModel) -> Unit)
        var onClickedWasCalled = false

        beforeEach {
            mockActivityService = MockActivityService().create()
            mockStringConverter = mockk {
                every { convertToHtmlSpan(any()) } returns mockk()
            }
            mockOrderViewModel = mockk()
            mockOnClicked = {
                onClickedWasCalled = true
            }
            mockRepository = mockk()

            uut = OrderViewHolderViewModel(
                activityService = mockActivityService,
                stringConverter = mockStringConverter,
                orderViewModel = mockOrderViewModel,
                onItemClicked = mockOnClicked,
                repository = mockRepository
            )
            mockData = mockk()
            mockResponse = mockk {
                every { errors } returns null
                every { data } returns mockData
            }
            coEvery { mockRepository.queryShippingDetails(any()) } returns mockResponse
        }

        describe("when .queryShippingDetails()") {
            describe("when the response does NOT have errors") {
                beforeEach {
                    mockData = mockk()
                    mockResponse = mockk {
                        every { errors } returns null
                        every { data } returns mockData
                    }
                    coEvery { mockRepository.queryShippingDetails(any()) } returns mockResponse
                    uut.setUp {}
                }
                it("sets shipping details to 'some fields'") {
                    mockResponse shouldNotBe null
                }
            }
        }

        describe("when .getCustomerText()") {

            describe("when .selectedOrder.shipToName has value") {

                var response: Spanned? = null

                beforeEach {
                    every { mockOrderViewModel.shipToName } returns "someShipToName"

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
                    every { mockOrderViewModel.shipToName } returns null

                    response = uut.getCustomerText()
                }

                it("returns null") {
                    response shouldBe null
                }
            }
        }

        describe("when .getOrderText()") {

            describe("when .selectedOrder.getOrderText() has value") {

                var response: Spanned? = null

                beforeEach {
                    every { mockOrderViewModel.getOrderText() } returns "orderId.invoiceId"

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
                    every { mockOrderViewModel.getOrderText() } returns null

                    response = uut.getOrderText()
                }

                it("returns null") {
                    response shouldBe null
                }
            }
        }

        describe("when .cardTapped()") {
            beforeEach {
                every { mockActivityService.hideKeyboard() } returns Unit
                uut.cardTapped()
            }

            it("calls .activityService.hideKeyboard()") {
                verify { mockActivityService.hideKeyboard() }
            }

            it("calls .onItemClicked()") {
                onClickedWasCalled shouldBe true
            }
        }
    }
})