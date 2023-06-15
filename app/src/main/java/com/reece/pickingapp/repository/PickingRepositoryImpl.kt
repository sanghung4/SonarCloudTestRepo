package com.reece.pickingapp.repository

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.reece.pickingapp.*
import com.reece.pickingapp.models.ProductDTO
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.type.*
import javax.inject.Inject

class PickingRepositoryImpl @Inject constructor(
    private val pickingApi: PickingApi
): PickingRepository {

    override suspend fun queryPickingTasks(
        branchId: String,
        userId: String
    ): Response<PickingOrdersQuery.Data>? {
        val input = PickingOrderInput(branchId, userId)
        return pickingApi.getApolloClient()?.query(PickingOrdersQuery(input))?.await()
    }

    override suspend fun queryPickingProductImage(
        productId: String
    ): Response<PickImageQuery.Data>? {
        return pickingApi.getApolloClient()?.query(PickImageQuery(input = productId))?.await()
    }

    override suspend fun queryUserTasks(
        branchId: String,
        userId: String,
        orderId: String
    ): Response<PickTasksQuery.Data>? {
        val input = PickingOrderInput(branchId, userId, orderId.toInput())
        return pickingApi.getApolloClient()?.query(PickTasksQuery(input = input))?.await()
    }

    override suspend fun mutationVerifyEclipseCredentials(
        username: String,
        password: String
    ): Response<VerifyEclipseCredentialsMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(VerifyEclipseCredentialsMutation(username, password))?.await()
    }

    override suspend fun mutationCompletePick(product: ProductDTO): Response<CompleteUserPickMutation.Data>? {
        val input = product.toCompletePickInput()
        return pickingApi.getApolloClient()?.mutate(CompleteUserPickMutation(input = input))?.await()
    }

    override suspend fun mutationUpdateProductSerialNumbers(input: UpdateProductSerialNumbersInput): Response<UpdateProductSerialNumbersMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(UpdateProductSerialNumbersMutation(input = input))?.await()
    }

    override suspend fun mutationAssignPickTask(input: PickingTaskInput): Response<AssignPickTaskMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(AssignPickTaskMutation(input = input))?.await()
    }

    override suspend fun mutationStagePickTasks(input: StagePickTaskInput): Response<StagePickTasksMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(StagePickTasksMutation(input = input))?.await()
    }

    override suspend fun mutationPostPackageData(input: StagePickTotePackagesInput): Response<StagePickTotePackagesMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(StagePickTotePackagesMutation(input = input))?.await()
    }

    override suspend fun mutationCloseTask(input: CloseTaskInput): Response<CloseTaskMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(CloseTaskMutation(input = input))?.await()
    }
    override suspend fun mutationSplitQuantity(input: SplitQuantityInput): Response<SplitQuantityMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(SplitQuantityMutation(input = input))?.await()
    }

    override suspend fun mutationCloseOrder(input: CloseOrderInput): Response<CloseOrderMutation.Data>? {
        return pickingApi.getApolloClient()?.mutate(CloseOrderMutation(input = input))?.await()
    }

    override suspend fun queryShippingDetails(input: ShippingDetailsKourierInput): Response<ShippingDetailsQuery.Data>? {
        return pickingApi.getApolloClient()?.query(ShippingDetailsQuery(input = input))?.await()
    }

    override suspend fun queryValidateBranch(input: ValidateBranchInput): Response<ValidateBranchQuery.Data>? {
        return pickingApi.getApolloClient()?.query(ValidateBranchQuery(input = input))?.await()
    }
}