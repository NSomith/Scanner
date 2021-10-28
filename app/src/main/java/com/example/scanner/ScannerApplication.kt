package com.example.scanner

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ScannerApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ScannerApplication)
            modules()
        }
    }
}