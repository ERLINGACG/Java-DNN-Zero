package com.erling.core.load.jna

import com.erling.core.load.ann.DyLinkLibInf
import com.erling.core.load.ann.InitType
import com.erling.core.load.ann.JdzFramework
import com.erling.core.load.exception.NotConfigPathException
import com.erling.core.load.exception.NotLibPathException
import com.sun.jna.Native
import java.io.File

fun loadLib(obj: Any) {

    val osName = System.getProperty("os.name")
    val libType = when {
        osName.startsWith("Windows") -> ".dll"
        osName.startsWith("Linux") -> ".so"
        osName.startsWith("Mac") -> ".dylib"
        else -> error("Unsupported OS")
    }

    val objFields = obj.javaClass.declaredFields
    objFields.forEach { field ->
        field.isAccessible = true
        val isFrame = field.getAnnotation(JdzFramework::class.java) ?: return@forEach

        val rootPath= isFrame.rootPath
        val libName = isFrame.name
        val isConfig   = isFrame.isConfig
        val configPath =isFrame.configPath

        if(rootPath == "" || libName == "") {
            throw NotLibPathException("rootPath or libName is not Complete")
        }
        if(isConfig && configPath == "") {
            throw NotConfigPathException("configPath is not Complete")
        }

        val mapping = isFrame.mapping

        val libPath = File(rootPath, libName + libType).absolutePath
        var fObj = field.get(obj)

        if(fObj == null) {
            when{
                !isConfig -> {
                    val constructor = field.type.getDeclaredConstructor()
                    constructor.isAccessible = true
                    fObj = constructor.newInstance()
                    field.set(obj, fObj)
                }
                isConfig -> {
                    val constructor = field.type.getDeclaredConstructor(String::class.java)
                    constructor.isAccessible = true
                    fObj = constructor.newInstance(configPath)
                    field.set(obj, fObj)
                }
            }
        }

        val fObjFields = fObj.javaClass.declaredFields
        fObjFields.forEach { objField ->
            objField.isAccessible = true
            objField.getAnnotation(DyLinkLibInf::class.java) ?: return@forEach
            val instance = Native.load(libPath, mapping.java)?: return@forEach
            objField.set(fObj, instance)

        }

        val fObjMethods = fObj.javaClass.declaredMethods
        fObjMethods.forEach { method ->
            method.isAccessible = true
            method.getAnnotation(InitType::class.java) ?: return@forEach
            method.invoke(fObj)
        }
    }
}