package com.example.android

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context


class Helper {
    companion object {

        fun isAppRunning(context: Context, packageName: String): Boolean {
            val activityManager: ActivityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val procInfos: List<RunningAppProcessInfo> = activityManager.runningAppProcesses
            if (procInfos != null) {
                for (processInfo in procInfos) {
                    if (processInfo.processName == packageName) {
                        return true
                    }
                }
            }
            return false
        }
    }

}