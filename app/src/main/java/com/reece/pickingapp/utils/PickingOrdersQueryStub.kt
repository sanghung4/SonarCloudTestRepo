package com.reece.pickingapp.utils

import com.apollographql.apollo.api.Response
import com.reece.pickingapp.PickingOrdersQuery
import com.reece.pickingapp.type.PickingOrderInput

class PickingOrdersQueryStub {
    /**
     * Generates a Response<PickingOrdersQuery.Data> to be used as response to PickingRepository.queryPickingTasks() or PickingRepository.queryUserTasks()
     *
     * Randomly generates shipToName (customer name) and shipVia
     *
     * @param numberOfOrders the number of orders returned as List<PickingOrdersQuery.PickingOrder>
     * @param branchId optional branchId defaults to "someBranchId"
     * @param userId optional userId defaults to "someUserId"
     * @return Response<PickingOrdersQuery.Data>
     *
     * EXAMPLE: see PickingOrderViewModel > queryOrdersList()
     *  REPLACE > val response = repository.queryPickingTasks(branchId, userId)
     *  WITH > val stubResponse = PickingOrdersQueryStub().generate(numberOfOrders = 50, branchId = branchId, userId = userId)
     *  PASS IN VALUE > _ordersList.postValue(ViewState.Success(stubResponse))
     */

    fun generate(numberOfOrders: Int, branchId: String? = null, userId: String? = null): Response<PickingOrdersQuery.Data> {
        val branch = branchId ?: "someBranchId"
        val user = userId ?: "someUserId"
        var pickingOrders = mutableListOf<PickingOrdersQuery.PickingOrder>()
        var totalOrders = 0
        if (numberOfOrders > 0) {
            totalOrders = numberOfOrders - 1
        }

        for (i in 0..totalOrders) {
            val num = i + 1
            pickingOrders.add(
                i,
                PickingOrdersQuery.PickingOrder(
                    orderId = (num * 1234).toString(),
                    generationId = numberOfOrders,
                    invoiceId = "Invoice$num",
                    branchId = branch,
                    pickGroup = "pickGroup$num",
                    assignedUserId = null,
                    billTo = numberOfOrders,
                    shipTo = numberOfOrders,
                    shipToName = generateCustomer(orderIndex = i),
                    pickCount = "pickCount$num", // total number of unique products that need to be picked
                    shipVia = generateShipVia(orderIndex = i),
                    isFromMultipleZones = false,
                    taskState = "taskState$num", // OPEN / ASSIGNED / CLOSED
                    taskWeight = num.toDouble()))
        }

        return Response(
            operation = PickingOrdersQuery(input = PickingOrderInput(branchId = branch, userId = user)),
            data = PickingOrdersQuery.Data(pickingOrders = pickingOrders)
        )
    }

    private fun generateShipVia(orderIndex: Int): String {
        // 52+ Total Possibilities in Eclipse
        val shipping = listOf("FED EX", "PICK UP", "USPS", "UPS", "CARRIER PIGEON", "DRAGON", "BOBS SHIPPING" )

        return shipping[orderIndex % shipping.size]
    }

    private fun generateCustomer(orderIndex: Int): String {
        val customer = listOf("Britland Knowles", "Dietrichson Kearney", "Jessup Kowalski", "Dominique Krueger", "Szell Knox", "West Kraft", "Gacy Kirwin", "Shipman Keller", "Randy Krause", "Bates Kennedy", "Nilsen Koch", "Haarmann Koenig", "Palpatine Kauffman", "Harvey Keeling", "Fred Kelsey", "Rose Kurtz", "Juan Kincaid", "Sutcliffe Kendrick", "Fritz Key", "Ian Kessler")

        return customer[orderIndex % customer.size].uppercase()
    }
}