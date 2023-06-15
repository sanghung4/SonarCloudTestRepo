package com.reece.pickingapp.interfaces

import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Optional methods available to the Interface.
 * Interface is added as a dependency to a ViewModel
 */
interface BarCodeScanInterface {
    fun onClickScan(id: Int, hint: String) {}

    fun onBarcodeScanned(barcode: String, barcodes: List<Barcode>, id: Int, hint: String) {}

}