package com.reece.pickingapp.repository

import com.apollographql.apollo.api.Response
import com.reece.pickingapp.*
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.type.*


interface PickingRepository {
    suspend fun queryPickingTasks(branchId: String, userId: String): Response<PickingOrdersQuery.Data>?
    suspend fun queryPickingProductImage(productId: String): Response<PickImageQuery.Data>?
    suspend fun queryUserTasks(branchId: String, userId: String, orderId: String): Response<PickTasksQuery.Data>?
    suspend fun mutationVerifyEclipseCredentials(username: String, password: String): Response<VerifyEclipseCredentialsMutation.Data>?
    suspend fun mutationCompletePick(product: ProductDTO): Response<CompleteUserPickMutation.Data>?
    suspend fun mutationAssignPickTask(input: PickingTaskInput): Response<AssignPickTaskMutation.Data>?
    suspend fun mutationUpdateProductSerialNumbers(input: UpdateProductSerialNumbersInput): Response<UpdateProductSerialNumbersMutation.Data>?
    suspend fun mutationStagePickTasks(input: StagePickTaskInput): Response<StagePickTasksMutation.Data>?
    suspend fun mutationPostPackageData(input: StagePickTotePackagesInput): Response<StagePickTotePackagesMutation.Data>?
    suspend fun mutationCloseTask(input: CloseTaskInput): Response<CloseTaskMutation.Data>?
    suspend fun mutationSplitQuantity(input: SplitQuantityInput): Response<SplitQuantityMutation.Data>?
    suspend fun mutationCloseOrder(input: CloseOrderInput): Response<CloseOrderMutation.Data>?
    suspend fun queryShippingDetails(input: ShippingDetailsKourierInput): Response<ShippingDetailsQuery.Data>?
    suspend fun queryValidateBranch(input: ValidateBranchInput): Response<ValidateBranchQuery.Data>?
}