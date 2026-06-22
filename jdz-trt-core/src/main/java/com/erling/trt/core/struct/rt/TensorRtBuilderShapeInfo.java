package com.erling.trt.core.struct.rt;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

//struct TensorRtBuilderShapeInfo{
//char*    name;
//
//int64_t  K_min_dims;
//int64_t* K_min_shape;
//
//int64_t  K_max_dims;
//int64_t* K_max_shape;
//
//int64_t  K_opt_dims;
//int64_t* K_opt_shape;
//};
public class TensorRtBuilderShapeInfo extends NativeStruct<TensorRtBuilderShapeInfo> {

    public C_POINTER name;

    public C_INT64 K_min_dims;
    public C_POINTER K_min_shape;

    public C_INT64 K_max_dims;
    public C_POINTER K_max_shape;

    public C_INT64 K_opt_dims;
    public C_POINTER K_opt_shape;



    public TensorRtBuilderShapeInfo(Arena arena) {
        super(TensorRtBuilderShapeInfo.class, arena);
        initFields(this);
    }

    public TensorRtBuilderShapeInfo setName(String name){
        this.name.set(name);
        return this;
    }


    public TensorRtBuilderShapeInfo setK_min_dims(Long K_min_dims){
        this.K_min_dims.set(K_min_dims);
        return this;
    }

    public TensorRtBuilderShapeInfo setK_min_shape(long[] K_min_shape) {
        this.K_min_shape.set(K_min_shape);
        return this;
    }
    public TensorRtBuilderShapeInfo setK_max_dims(Long K_max_dims){
        this.K_max_dims.set(K_max_dims);
        return this;
    }
    public TensorRtBuilderShapeInfo setK_max_shape(long[] K_max_shape) {
        this.K_max_shape.set(K_max_shape);
        return this;
    }
    public TensorRtBuilderShapeInfo setK_opt_dims(Long K_opt_dims){
        this.K_opt_dims.set(K_opt_dims);
        return this;
    }
    public TensorRtBuilderShapeInfo setK_opt_shape(long[] K_opt_shape) {
        this.K_opt_shape.set(K_opt_shape);
        return this;
    }




}
