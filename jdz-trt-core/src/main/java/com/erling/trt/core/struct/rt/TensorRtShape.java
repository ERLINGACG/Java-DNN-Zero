package com.erling.trt.core.struct.rt;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_INT64;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;
import java.util.List;
import java.util.function.Supplier;

import static com.erling.core.load.ffm.api.cpp.struct.StructRegisterKt.initFields;

public class TensorRtShape extends NativeStruct<TensorRtShape> {

    public C_POINTER name;
    public C_INT64 dims;
    public C_POINTER shape;
    public TensorRtShape(Arena arena) {
        super(TensorRtShape.class, arena);
        initFields(this);
    }

    public TensorRtShape setDims(Supplier<List<Long>> dimsSupplier){
        var dims=dimsSupplier.get();

        this.dims.set((long) dims.size());
        var data=new long[dims.size()];

        for(int i=0;i<dims.size();i++){data[i]=dims.get(i);}
        this.shape.set(data);
        return this;
    }

    public TensorRtShape setName(String name){
        this.name.set(name);
        return this;
    }
}
