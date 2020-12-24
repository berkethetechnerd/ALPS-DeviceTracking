package com.alpsproject.devicetracking.delegates

interface PermissionDelegate {
    fun permissionGranted()
    fun permissionRejected()
}