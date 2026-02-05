package common.utils

import platform.Foundation.NSUUID

internal actual fun generateUUID() = NSUUID().UUIDString()
