package com.konka.kkanimation

import android.app.Application
import android.os.Environment
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
        val SDCARD = Environment.getExternalStorageDirectory().absolutePath;
        val logPath = "$SDCARD/marssample/log";

// this is necessary, or may crash for SIGBUS
        val cachePath = "${this.filesDir}/xlog"

//init xlog
        if (BuildConfig.DEBUG) {
            Xlog.appenderOpen(
                Xlog.LEVEL_DEBUG,
                Xlog.AppednerModeAsync,
                cachePath,
                logPath,
                "MarsSample",
                0,
                ""
            );
            Xlog.setConsoleLogOpen(true);

        } else {
            Xlog.appenderOpen(
                Xlog.LEVEL_INFO,
                Xlog.AppednerModeAsync,
                cachePath,
                logPath,
                "MarsSample",
                0,
                ""
            );
            Xlog.setConsoleLogOpen(false);
        }
        Log.setLogImp(Xlog ())
    }
}