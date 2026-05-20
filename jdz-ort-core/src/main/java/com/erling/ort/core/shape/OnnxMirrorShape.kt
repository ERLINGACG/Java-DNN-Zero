package com.erling.ort.core.shape

import com.erling.ort.core.type.OnnxDataType

data class OnnxMirrorShape<T>(
    val shape: LongArray = longArrayOf(),
    val name: String? = "AUTO",
    val type: OnnxDataType? = OnnxDataType.UNKNOWN,
    val data: T
) {

}