package com.app.ads

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.ads.MobileAds

@RequiresPermission(Manifest.permission.INTERNET)
actual fun initializeAds(context: Any?) {
    if (!BuildConfig.IS_ADS_ENABLED) return
    val androidContext = context as? Context ?: return
    MobileAds.initialize(androidContext)
}
