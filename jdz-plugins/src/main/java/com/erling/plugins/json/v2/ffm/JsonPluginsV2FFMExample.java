package com.erling.plugins.json.v2.ffm;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class JsonPluginsV2FFMExample {





    public void load() throws Throwable {
        StructLayout JSON_DATA_LAYOUT = MemoryLayout.structLayout(
                ValueLayout.ADDRESS.withName("json_root"),   // 偏移 0
                ValueLayout.ADDRESS.withName("json_now"),    // 偏移 8
                ValueLayout.ADDRESS.withName("json_value"),  // 偏移 16
                ValueLayout.JAVA_INT.withName("now_level"),  // 偏移 24
                MemoryLayout.paddingLayout(4)                // 偏移 28，对齐到 32
        ).withName("JsonData");

        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = SymbolLookup.libraryLookup(
                "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\share\\GeneralDnnLib_Zero_plugin_Json_v2.dll", Arena.ofAuto()
        );
        var func      = lookup.find("BuildJsonData").orElseThrow();
        var printFunc = lookup.find("PrintJsonData").orElseThrow();

        FunctionDescriptor build_descriptor = FunctionDescriptor.of(
                ValueLayout.JAVA_INT,          // 返回值 int
                ValueLayout.ADDRESS,           // 参数1: JsonData*
                ValueLayout.ADDRESS            // 参数2: const char*
        );

        FunctionDescriptor print_descriptor = FunctionDescriptor.of(
                ValueLayout.JAVA_INT,          // 返回值 int
                ValueLayout.ADDRESS             // 参数1: JsonData*
        );


        MethodHandle buildJsonData = Linker.nativeLinker().downcallHandle(func, build_descriptor);
        MethodHandle printJsonData = Linker.nativeLinker().downcallHandle(printFunc, print_descriptor);

        // 4. 函数描述：无参、无返回值

        // 6. 调用！
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment jsonDataSegment = arena.allocate(JSON_DATA_LAYOUT);
            // 可选：将结构体成员初始化为 NULL（allocate 已清零，无需额外操作）

            // 6. 准备 JSON 字符串
            String jsonStr = "{\"key\":\"value\"}";
            MemorySegment cJsonStr = arena.allocateUtf8String(jsonStr);

            // 7. 调用 BuildJsonData
            int result = (int) buildJsonData.invokeExact(jsonDataSegment, cJsonStr);

            if (result == 0) {
                // 读取 now_level 等字段（如果需要）
               int printResult = (int) printJsonData.invokeExact(jsonDataSegment);
               if (printResult == 0) {
                   System.out.println("Print success");
               } else {
                   System.err.println("PrintJsonData failed, code = " + printResult);
               }
            }
        }
    }
}
