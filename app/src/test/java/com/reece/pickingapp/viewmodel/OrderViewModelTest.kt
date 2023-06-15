package com.reece.pickingapp.viewmodel

import com.reece.pickingapp.models.OrderDTO
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every

class OrderViewModelTest : DescribeSpec({
    describe("OrderViewModel") {
        lateinit var uut: OrderViewModel
        lateinit var orderDTO: OrderDTO

        beforeEach {
            orderDTO = OrderDTO(
                orderId = "someOrderId",
                generationId = 0,
                invoiceId = "someInvoiceId",
                branchId = "someBranchId",
                pickGroup = "somePickGroup",
                assignedUserId = "someAssignedUserId",
                billTo = 0,
                shipTo = 0,
                shipToName = "someShipToName",
                pickCount = "somePickCount",
                shipVia = "someShipVia",
                isFromMultipleZones = false,
                taskState = "someTaskState",
                taskWeight = 1.0
            )
            uut = OrderViewModel(order = orderDTO)
        }

        it("sets .orderId to 'someOrderId'") {
            uut.orderId shouldBe "someOrderId"
        }

        it("sets .shipToName to 'someShipToName'") {
            uut.shipToName shouldBe "someShipToName"
        }

        it("sets .branchId to 'someBranchId'") {
            uut.branchId shouldBe "someBranchId"
        }

        it("sets .assignedUserId to 'someAssignedUserId'") {
            uut.assignedUserId shouldBe "someAssignedUserId"
        }

        it("sets .invoiceId to 'someInvoiceId'") {
            uut.invoiceId shouldBe "someInvoiceId"
        }

        describe("when .getOrderDTO()") {

            var result: OrderDTO? = null

            beforeEach {
                result = uut.getOrderDTO()
            }

            it("returns the OrderDTO") {
                result shouldBe orderDTO
            }
        }

        describe("when .getToteName()") {

            var result: String? = null

            beforeEach {
                result = uut.getToteName()
            }

            it("returns 'TOTEsomeOrderId") {
                result shouldBe "TOTEsomeOrderId"
            }
        }

        describe("when .getOrderText()") {

            var result: String? = null

            beforeEach {
                result = uut.getOrderText()
            }

            it("returns 'someOrderId.someInvoiceId") {
                result shouldBe "someOrderId.someInvoiceId"
            }

            describe("when orderId.isNullOrEmpty() and "){
                beforeEach {
                    orderDTO = OrderDTO(
                        orderId = "",
                        generationId = 0,
                        invoiceId = "someInvoiceId",
                        branchId = "someBranchId",
                        pickGroup = "somePickGroup",
                        assignedUserId = "someAssignedUserId",
                        billTo = 0,
                        shipTo = 0,
                        shipToName = "someShipToName",
                        pickCount = "somePickCount",
                        shipVia = "someShipVia",
                        isFromMultipleZones = false,
                        taskState = "someTaskState",
                        taskWeight = 1.0
                    )
                    uut = OrderViewModel(order = orderDTO)
                    result = uut.getOrderText()
                }

                it("Result should be empty String "){
                    result shouldBe null
                }
            }
        }



        describe("when .getOrderIdValue()") {

            var result: String? = null

            beforeEach {
                result = uut.getOrderIdValue()
            }

            it("returns 'someOrderId.001") {
                result shouldBe "someOrderId.001"
            }


            describe("when orderId.isNullOrEmpty()"){
                beforeEach {
                    orderDTO = OrderDTO(
                        orderId = "",
                        generationId = 0,
                        invoiceId = "someInvoiceId",
                        branchId = "someBranchId",
                        pickGroup = "somePickGroup",
                        assignedUserId = "someAssignedUserId",
                        billTo = 0,
                        shipTo = 0,
                        shipToName = "someShipToName",
                        pickCount = "somePickCount",
                        shipVia = "someShipVia",
                        isFromMultipleZones = false,
                        taskState = "someTaskState",
                        taskWeight = 1.0
                    )
                    uut = OrderViewModel(order = orderDTO)
                    result = uut.getOrderIdValue()
                }

                it("Result should be empty String "){
                    result shouldBe ""
                }
            }
        }
    }
})