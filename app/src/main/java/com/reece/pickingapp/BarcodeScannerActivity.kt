package com.reece.pickingapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mlkitqrandbarcodescanner.BarCodeAndQRCodeAnalyser
import com.google.mlkit.vision.barcode.common.Barcode
import com.reece.pickingapp.databinding.ActivityBarcodeScannerBinding
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.*


val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
const val RATIO_4_3_VALUE = 4.0 / 3.0
const val RATIO_16_9_VALUE = 16.0 / 9.0
typealias BarcodeAnalyzerListener = (barcode: MutableList<Barcode>) -> Unit

class BarcodeScannerActivity : AppCompatActivity() {

    private val executor by lazy {
        Executors.newSingleThreadExecutor()
    }

    private val multiPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.entries.size < 1) {
                Toast.makeText(this, "Please Accept all the permissions", Toast.LENGTH_SHORT).show()
            } else {
                binding.previewView.post {
                    startCamera()
                }
            }

        }

    private var processingBarcode = AtomicBoolean(false)
    private lateinit var binding: ActivityBarcodeScannerBinding
    private lateinit var cameraInfo: CameraInfo
    private lateinit var cameraControl: CameraControl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_barcode_scanner)

        // Request camera permissions
        multiPermissionCallback.launch(
            REQUIRED_PERMISSIONS
        )
        if (allPermissionsGranted()) {
            binding.previewView.post {
                //Initialize graphics overlay
                startCamera()
            }
        } else {
            requestAllPermissions()
        }
    }


    @SuppressLint("UnsafeExperimentalUsageError")
    private fun startCamera() {
        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { binding.previewView.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = binding.previewView.display.rotation

        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({

            // CameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

            preview.setSurfaceProvider(binding.previewView.surfaceProvider)

            // ImageAnalysis
            val textBarcodeAnalyzer = initializeAnalyzer(screenAspectRatio, rotation)
            cameraProvider.unbindAll()

            try {
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, textBarcodeAnalyzer
                )
                cameraControl = camera.cameraControl
                cameraInfo = camera.cameraInfo
                cameraControl.setLinearZoom(0.5f)


            } catch (exc: Exception) {
                exc.printStackTrace()
                //Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }


    private fun requestAllPermissions() {
        multiPermissionCallback.launch(
            REQUIRED_PERMISSIONS
        )
    }

    /**
     * Check if all permission specified in the manifest have been granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun initializeAnalyzer(screenAspectRatio: Int, rotation: Int): UseCase {
        return ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(rotation)
            .build()
            .also {

                it.setAnalyzer(executor, BarCodeAndQRCodeAnalyser { barcode ->
                    /**
                     * Change update  to true if you want to scan only one barcode or it will continue scaning after detecting for the first time
                     */
                    /**
                     * Change update  to true if you want to scan only one barcode or it will continue scaning after detecting for the first time
                     */
                    /**
                     * Change update  to true if you want to scan only one barcode or it will continue scaning after detecting for the first time
                     */

                    /**
                     * Change update  to true if you want to scan only one barcode or it will continue scaning after detecting for the first time
                     */
                    if (processingBarcode.compareAndSet(false, false)) {
                        onBarcodeDetected(barcode)
                    }
                })
            }
    }


    private fun onBarcodeDetected(barcodes: List<Barcode>) {
        if (barcodes.isNotEmpty()) {
            val barcode = barcodes[0].rawValue
            if (barcodes[0].format != Barcode.FORMAT_QR_CODE) {
               // Toast.makeText(this, "Bar code Detected > $barcode", Toast.LENGTH_SHORT).show()

                val intent = intent
                intent.putExtra("barcode",barcode)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
