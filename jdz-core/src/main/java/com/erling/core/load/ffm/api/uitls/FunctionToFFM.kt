package com.erling.core.load.ffm.api.uitls

import com.erling.core.load.ffm.api.pojo.FunctionArray
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.SymbolLookup
import java.lang.foreign.ValueLayout
import java.lang.invoke.MethodHandle
import kotlin.collections.mutableListOf

fun jsonToFFM(libPath: String, functionFFM: FunctionArray) : HashMap<String, MethodHandle> {
    val lookup: SymbolLookup = SymbolLookup.libraryLookup(
        libPath, java.lang.foreign.Arena.ofAuto()
    )
    val funList     = HashMap<String, MethodHandle>()
   functionFFM.functions.forEach {

       val args = mutableListOf<ValueLayout>()

       it.args.forEach { it1 ->
           when (it1) {
               "int" -> {
                   args.add(ValueLayout.JAVA_INT)
               }
               "float" -> {
                   args.add(ValueLayout.JAVA_FLOAT)
               }
               "double" -> {
                   args.add(ValueLayout.JAVA_DOUBLE)
               }
               "char" -> {
                   args.add(ValueLayout.JAVA_CHAR)
               }
               "pointer" -> {
                   args.add(ValueLayout.ADDRESS)
               }
               else -> {
                   throw IllegalArgumentException("argType $it1 not support")
               }
           }

       }

       val findName = lookup.find(it.name).orElseThrow()
       var functionDescriptor: FunctionDescriptor

       when (it.returnType) {
           "void" -> {
               functionDescriptor = FunctionDescriptor.ofVoid(*args.toTypedArray())
           }
           "int" -> {
               functionDescriptor = FunctionDescriptor.of(
                   ValueLayout.JAVA_INT,
                   *args.toTypedArray()
               )
           }
           "float" -> {
               functionDescriptor = FunctionDescriptor.of(
                   ValueLayout.JAVA_FLOAT,
                   *args.toTypedArray()
               )
           }
           "double" -> {
               functionDescriptor = FunctionDescriptor.of(
                   ValueLayout.JAVA_DOUBLE,
                   *args.toTypedArray()
               )
           }
           "char" -> {
               functionDescriptor = FunctionDescriptor.of(
                   ValueLayout.JAVA_CHAR,
                   *args.toTypedArray()
               )
           }
           "pointer" -> {
               functionDescriptor = FunctionDescriptor.of(
                   ValueLayout.ADDRESS,
                   *args.toTypedArray()
               )
           }
           else -> {
               throw IllegalArgumentException("returnType $it.returnType not support")
           }
       }
       funList[it.name] = Linker.nativeLinker().downcallHandle(findName, functionDescriptor)
   }

  return funList
}

fun putFFM(path : String, name: String, args: MutableList<ValueLayout>, returnType: String) : MethodHandle {
    val lookup: SymbolLookup = SymbolLookup.libraryLookup(path, java.lang.foreign.Arena.ofAuto())
    val findName = lookup.find(name).orElseThrow()
    val functionDescriptor: FunctionDescriptor
    when (returnType) {
        "void" -> {
            functionDescriptor = FunctionDescriptor.ofVoid(*args.toTypedArray())
        }
        "int" -> {
            functionDescriptor = FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                *args.toTypedArray()
            )
        }
        "float" -> {
            functionDescriptor = FunctionDescriptor.of(
                ValueLayout.JAVA_FLOAT,
                *args.toTypedArray()
            )
        }
        "double" -> {
            functionDescriptor = FunctionDescriptor.of(
                ValueLayout.JAVA_DOUBLE,
                *args.toTypedArray()
            )
        }
        "MemorySegment" -> {
            functionDescriptor = FunctionDescriptor.of(
                ValueLayout.ADDRESS,
                *args.toTypedArray()
            )
        }
        else -> {
            throw IllegalArgumentException("returnType $returnType not support")
        }

    }

    return Linker.nativeLinker().downcallHandle(findName, functionDescriptor);
}
