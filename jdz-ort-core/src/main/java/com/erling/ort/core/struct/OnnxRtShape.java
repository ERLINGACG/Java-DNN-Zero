package com.erling.ort.core.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct OnnxRtShape{
//std::unique_ptr<char[]> name_ptr;
//std::unique_ptr<void*>  shape_ptr;
//std::unique_ptr<void*>  data_ptr;
//int64_t shape_size;
//int64_t data_size;
//int32_t data_type;
//    };
public class OnnxRtShape extends NativeStruct<OnnxRtShape> {

    public C_POINTER name_ptr;
    public C_POINTER shape_ptr;
    public C_POINTER data_ptr;

    public C_INT64 shape_size;
    public C_INT64 data_size;
    public C_INT data_type;

    public OnnxRtShape(Arena arena) {
        super(OnnxRtShape.class, arena);
        initFields(this);
    }

    public OnnxRtShape shape(long[] shape){
        this.name_ptr.set("AUTO");
        this.shape_ptr.set(shape);
        this.shape_size.set((long) shape.length);
        return this;
    }
    public OnnxRtShape shape(String name,long[] shape){
        this.name_ptr.set(name);
        this.shape_ptr.set(shape);
        this.shape_size.set((long) shape.length);
        return this;
    }

    public OnnxRtShape data(long[] data_ptr){
        this.data_ptr.set(data_ptr);
        this.data_size.set((long) data_ptr.length);
        return this;
    }

    public OnnxRtShape data(short[] data_ptr){
        this.data_ptr.set(data_ptr);
        this.data_size.set((long) data_ptr.length);
        return this;
    }

    public OnnxRtShape type(int data_type){
        this.data_type.set(data_type);
        return this;
    }
}
