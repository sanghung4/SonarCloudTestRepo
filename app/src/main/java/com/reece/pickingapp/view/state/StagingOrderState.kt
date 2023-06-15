package com.reece.pickingapp.view.state

sealed class StagingOrderState {
    object StageReady : StagingOrderState()
    object Default : StagingOrderState()
}