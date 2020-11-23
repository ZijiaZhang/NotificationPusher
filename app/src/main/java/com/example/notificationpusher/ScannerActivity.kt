package com.example.notificationpusher

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.jar.Manifest


class ScannerActivity : Activity(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)

        if (ContextCompat.checkSelfPermission(this, CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), 1);
        }
        mScannerView = ZXingScannerView(this) // Programmatically initialize the scanner view
        setContentView(mScannerView) // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView?.startCamera() // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera() // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        // Do something with the result here
        Log.v(TAG, rawResult.text) // Prints scan results
        Log.v(TAG, rawResult.barcodeFormat.toString()) // Prints the scan format (qrcode, pdf417 etc.)
        val intnet = Intent()
        intnet.putExtra("code", rawResult.text)
        setResult(0, intnet)
        finish()
        // If you would like to resume scanning, call this method below:
    }
}