package com.erling.trt.core.struct.rt.data;

import com.erling.trt.core.en.TensorRTDataEnum;
import com.erling.trt.core.struct.cuda.TensorRTCudaAddress;
import com.erling.trt.core.struct.rt.TensorRtShape;

public class TensorRTData<T> {

    public T data;

    public TensorRTData<T> setDataType(TensorRTDataEnum dataType) {
        this.dataType = dataType;
        return this;
    }

    public TensorRTData<T> setData(T data) {
        this.data = data;
        return this;
    }

    public TensorRTData<T> setShape(TensorRtShape shape) {
        this.shape = shape;
        return this;
    }

    public TensorRTData<T> setAddress(TensorRTCudaAddress address) {
        this.address = address;
        return this;
    }

    public TensorRTDataEnum dataType;
    public TensorRtShape shape;
    public TensorRTCudaAddress address;

}
