package com.app.barksandmeows

import android.app.Application
import com.app.ads.initializeAds
import di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BarksAndMeowsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BarksAndMeowsApplication)
            modules(appModule())
        }
        initializeAds(this)
    }
}
