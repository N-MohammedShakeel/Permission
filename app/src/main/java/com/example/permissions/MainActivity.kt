package com.example.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSIONS = 101
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )
    private var permissionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.bt)
        button.setOnClickListener {
            requestNextPermission()
        }
    }

    private fun requestNextPermission() {
        if (permissionIndex < PERMISSIONS.size) {
            val permission = PERMISSIONS[permissionIndex]
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_PERMISSIONS)
            } else {
                // Permission already granted
                Toast.makeText(this, "$permission already granted", Toast.LENGTH_SHORT).show()
                permissionIndex++
                requestNextPermission() // Request the next permission
            }
        } else {
            Toast.makeText(this, "All permissions processed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            val permission = permissions[0]
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "$permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "$permission denied", Toast.LENGTH_SHORT).show()
            }
            permissionIndex++
            requestNextPermission() // Request the next permission
        }
    }
}
