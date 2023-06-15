package com.reece.pickingapp.viewmodel

import com.reece.pickingapp.models.ProductDTO
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

class ProductViewModelTest: DescribeSpec({
    describe("ProductViewModel") {
        lateinit var uut: ProductModel
        lateinit var mockProductDTO: ProductDTO

        beforeEach {
            mockProductDTO = mockk(relaxUnitFun = true) {
                every { orderId } returns "someOrderId"
                every { productId } returns "someProductId"
                every { description } returns "someDescription"
                every { location } returns "someLocation"
                every { quantity } returns 100
                every { isSerial } returns true
                every { warehouseID } returns "someWarehouseID"
                every { setOrderId() } returns "someOrderId"
            }
            uut = ProductModel(mockProductDTO)
        }

        describe("when .getProductDTO()") {
            var result: ProductDTO =  mockk()

            beforeEach {
                result = uut.getProductDTO()
            }

            it("returns the passed in product") {
                   result shouldBeSameInstanceAs mockProductDTO
            }

            it("when .setOrderId") {
                result.setOrderId() shouldBe  "someOrderId"
            }
        }

        describe("when .setTote()") {
            val toteSlot = slot<String>()

            beforeEach {
                every { mockProductDTO.tote = capture(toteSlot) } answers {}
                uut.setTote()
            }

            it("sets the tote to 'TOTEsomeOrderId'") {
                toteSlot.captured shouldBe "TOTEsomeOrderId"
            }
        }

        describe("when .setStartPickTime()") {
            val startPickTimeSlot = slot<String>()

            beforeEach {
                every { mockProductDTO.startPickTime = capture(startPickTimeSlot) } answers {}
                uut.setStartPickTime("somePickTime")
            }

            it("sets the startPickTime to 'somePickTime'") {
                startPickTimeSlot.captured shouldBe "somePickTime"
            }
        }

        describe("when  toString()"){
            val result:String? = null
            beforeEach {
                uut.toString()
            }
            it("value should not be null"){
                result shouldBe null
            }
        }
    }
})