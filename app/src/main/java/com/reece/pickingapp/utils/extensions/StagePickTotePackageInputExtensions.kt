package com.reece.pickingapp.utils.extensions

import com.reece.pickingapp.type.*

fun StagePickTaskInput.toStagePickTotePackageInput(packageList: MutableList<PackageInput>) =
    StagePickTotePackagesInput(
        orderId,
        invoiceId,
        branchId,
        tote,
        packageList
    )

fun StagePickTaskInput.toCloseTaskInput() =
    CloseTaskInput(
        orderId,
        invoiceId,
        branchId,
        tote = tote
    )