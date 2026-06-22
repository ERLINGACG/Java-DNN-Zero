package com.erling.core.load.ffm.api.loader

import com.erling.core.load.ann.JdzFrameFFM
import com.erling.core.load.ffm.api.cpp.hook.AutoInit
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy
import com.erling.core.load.ffm.api.cpp.hook.InitFunction
import com.erling.core.load.ffm.api.cpp.hook.SetConfig
import com.erling.core.load.ffm.api.uitls.jsonToFFM
import com.erling.core.load.ffm.api.uitls.putFFM
import com.erling.core.load.ffm.api.uitls.readJson
import java.io.File
import java.lang.foreign.MemorySegment
import java.lang.foreign.StructLayout
import java.lang.foreign.ValueLayout
import java.lang.invoke.MethodHandle
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.emptyArray

fun load(tagObject: Any){
    val libType = getLibType();

    val objFields = tagObject.javaClass.declaredFields

    objFields.forEach  { field ->
        field.isAccessible = true
        val isFrame = field.getAnnotation(JdzFrameFFM::class.java) ?: return@forEach
        val setConfig = field.getAnnotation(SetConfig::class.java);
        val rootPath = isFrame.rootPath
        val libName = isFrame.name + libType
        val libPath = File(rootPath, libName).absolutePath

        when (isFrame.useMappingConfig) {
            true -> {
                val json = readJson(File(isFrame.mapping).readText())

                var fieldObject= field.get(tagObject)
                if(fieldObject == null) {
                    val constructor = field.type.getDeclaredConstructor()
                    constructor.isAccessible = true
                    fieldObject = constructor.newInstance()
                    field.set(tagObject, fieldObject)
                }

                fieldObject.javaClass.declaredMethods.forEach { method ->
                    method.getAnnotation(InitFunction::class.java) ?: return@forEach
                    method.isAccessible = true
                    val funMap = jsonToFFM(libPath, json)
                    method.invoke(fieldObject, funMap)
                }

            }
            false -> {
                    var fieldObject= field.get(tagObject)

                    if(fieldObject == null) {
                        val constructor = field.type.getDeclaredConstructor()
                        constructor.isAccessible = true
                        fieldObject = constructor.newInstance()
                        field.set(tagObject, fieldObject)
                    }

                    val funMap = HashMap<String, MethodHandle>()
                    fieldObject.javaClass.fields.forEach { field ->
                        field.isAccessible = true
                        val creatProxy = field.getAnnotation(CreatProxy::class.java)

                        if(creatProxy != null) {
                            val args = mutableListOf<ValueLayout>()
                            creatProxy.mappingClass.java.declaredMethods.forEach { method ->
                                  method.isAccessible = true
                                  method.parameters.forEach { param ->
                                      when (param.type) {
                                          Int::class.java -> {
                                              args.add(ValueLayout.JAVA_INT)
                                          }
                                          Float::class.java -> {
                                              args.add(ValueLayout.JAVA_FLOAT)
                                          }
                                          Double::class.java -> {
                                              args.add(ValueLayout.JAVA_DOUBLE)
                                          }
                                          Long::class.java -> {
                                              args.add(ValueLayout.JAVA_LONG)
                                          }
                                          MemorySegment::class.java,
                                          -> {
                                              args.add(ValueLayout.ADDRESS)
                                          }
                                      }
                                  }
                                  val handle =  putFFM(
                                        libPath,
                                        method.name,
                                        args,
                                        method.returnType.simpleName ?: "void"
                                  )
                                  args.clear()
                                  funMap[method.name] = handle

                            }
                            val proxy = createNativeProxyForJava(
                                creatProxy.mappingClass.java,
                                funMap
                            )
                            field.set(fieldObject, proxy)
                        }


                    }

                    fieldObject.javaClass.declaredMethods.forEach { method ->

                        method.isAccessible = true
//                        println("Method: ${method.name}: ${method.getAnnotation(AutoInit::class.java)}")
                        if(method.getAnnotation(InitFunction::class.java) != null) {
                            method.invoke(fieldObject, funMap)
                        }

                        if(method.getAnnotation(AutoInit::class.java) != null) {
                            method.invoke(fieldObject, *(setConfig?.args ?: emptyArray()))
                        }
                    }
                }

            }
        }

}

// Kotlin 代码
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> createNativeProxy(
    functionMap: Map<String, MethodHandle>
): T {
    return Proxy.newProxyInstance(
        T::class.java.classLoader,
        arrayOf(T::class.java),
        InvocationHandler { _, method, args ->
            val handle = functionMap[method.name]
                ?: throw UnsupportedOperationException("No native function: ${method.name}")
            handle.invokeWithArguments(*(args ?: emptyArray()))
        }
    ) as T
}


fun createNativeProxyForJava(
    interfaceType: Class<*>,
    functionMap: Map<String, MethodHandle>
): Any {
    return Proxy.newProxyInstance(
        interfaceType.classLoader,              // 加载代理的类加载器
        arrayOf(interfaceType)      // 代理的接口类型
    )
    { _, method, args ->                     // 代理的调用处理程序
        val handle = functionMap[method.name]
            ?: throw UnsupportedOperationException("No native function: ${method.name}")
        handle.invokeWithArguments(*(args ?: emptyArray()))
    }
}


