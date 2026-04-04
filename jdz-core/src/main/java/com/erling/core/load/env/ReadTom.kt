package com.erling.core.load.env

import com.moandjiezana.toml.Toml
import java.io.File

fun readToml(path: String,key: String) : List<String> {
    val toml: Toml = Toml().read(File(path))
    return toml.getList(key)
}
