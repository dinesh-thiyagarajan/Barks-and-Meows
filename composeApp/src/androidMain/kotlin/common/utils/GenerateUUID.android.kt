package common.utils

import java.util.UUID

internal actual fun generateUUID() = UUID.randomUUID().toString()