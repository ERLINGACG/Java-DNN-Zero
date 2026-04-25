package com.erling.test.plugins.json.ffm;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class JsonData extends NativeStruct<JsonData> {


//    public MemorySegment json_root;
   public C_POINTER json_root;

//
    public C_POINTER json_now;
//
    public C_POINTER json_value;
//
    public C_INT now_level;

    public JsonData(Arena arena) {
        super(JsonData.class, arena);
//        initFields(this);
    }

    private JPluginsCppInf jPluginsCppInf;
    public JsonData setPluginsCppInf(JPluginsCppInf jPluginsCppInf) {
        this.jPluginsCppInf = jPluginsCppInf;
        return this;
    }

    public JsonData build(String json){
        jPluginsCppInf.BuildJsonData(
                this.getMemorySegment(),
                arena.allocateUtf8String(json)
        );
        return this;
    }

    public JsonData get(String key){
        jPluginsCppInf.PostToKey(
                this.getMemorySegment(),
                arena.allocateUtf8String(key),
                1
        );
        return this;
    }

    public JsonData get(int index){
        jPluginsCppInf.PostToKey(
                this.getMemorySegment(),
                arena.allocateUtf8String(String.valueOf(index)),
                0
        );
        return this;
    }

    public InfoStruct at(){
//        jPluginsCppInf.At(this.getMemorySegment());
        InfoStruct info = new InfoStruct(arena);
        jPluginsCppInf.GetValue(this.getMemorySegment(), info.getMemorySegment());
        return info;
    }

}
