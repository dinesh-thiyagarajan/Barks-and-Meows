package common.utils

import com.app.barksandmeows.BuildConfig

actual fun getAppVersion(): String = BuildConfig.VERSION_NAME
