package com.example.tasky.common.domain.util

import com.example.tasky.common.domain.error.DataError

fun Int.toNetworkErrorType(): DataError.Network {
    return when (this) {
        401 -> DataError.Network.UNAUTHORIZED
        408 -> DataError.Network.REQUEST_TIMEOUT
        409 -> DataError.Network.CONFLICT
        413 -> DataError.Network.PAYLOAD_TOO_LARGE
        else -> DataError.Network.UNKNOWN
    }
}