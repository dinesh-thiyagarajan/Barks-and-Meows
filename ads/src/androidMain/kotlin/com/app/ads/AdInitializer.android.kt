package com.app.ads

import android.content.Context
import com.google.android.gms.ads.MobileAds

actual fun initializeAds(context: Any?) {
    val androidContext = context as? Context ?: return
    MobileAds.initialize(androidContext)
}
