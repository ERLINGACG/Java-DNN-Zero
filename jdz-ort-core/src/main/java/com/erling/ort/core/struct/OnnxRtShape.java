package com.erling.ort.core.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;
import com.erling.ort.core.type.OnnxDataType;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//    char*     name;
//    void*     data;
//    int64_t*  shape;
//    int32_t   dim;
//    int32_t   data_len;
//    int32_t   data_type;
//    int32_t   shape_type;
public class OnnxRtShape extends NativeStruct<OnnxRtShape> {

    public C_POINTER name;

    public C_POINTER data;
    public C_POINTER shape;
    public C_INT dim;
    public C_INT data_len;
    public C_INT data_type;
    public C_INT shape_type;

    public OnnxRtShape(Arena arena) {
        super(OnnxRtShape.class, arena);
        initFields(this);
    }

    public OnnxRtShape setName(String name){
        this.name.set(name);
        return this;
    }

    public OnnxRtShape setData(int[] data){
        this.data.set(data);
        this.data_len.set(data.length);
        this.data_type.set(OnnxDataType.INT32.i());
        return this;
    }

    public OnnxRtShape setData(short[] data){
        this.data.set(data);
        this.data_len.set(data.length);
        this.data_type.set(OnnxDataType.INT16.i());
        return this;
    }

    public OnnxRtShape setData(float[] data){
        this.data.set(data);
        this.data_len.set(data.length);
        this.data_type.set(OnnxDataType.FLOAT.i());
        return this;
    }

    public OnnxRtShape setData(long[] data){
        this.data.set(data);
        this.data_len.set(data.length);
        this.data_type.set(OnnxDataType.INT64.i());
        return this;
    }

    public OnnxRtShape setShape(long[] shape){
        this.shape.set(shape);
        this.dim.  set(shape.length);
        return this;
    }

    public OnnxRtShape input(){
        this.shape_type.set(0);
        return this;
    }

    public OnnxRtShape output(){
        this.shape_type.set(1);
        return this;
    }






}
