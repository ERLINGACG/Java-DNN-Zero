package com.erling.core.load.ffm.api.loader

fun getLibType(): String {
    val osName = System.getProperty("os.name")
    val libType = when {
        osName.startsWith("Windows") -> ".dll"
        osName.startsWith("Linux") -> ".so"
        osName.startsWith("Mac") -> ".dylib"
        else -> error("Unsupported OS")
    }
    return libType

}
