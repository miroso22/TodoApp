package com.example.todo.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Context.hasPermission(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
