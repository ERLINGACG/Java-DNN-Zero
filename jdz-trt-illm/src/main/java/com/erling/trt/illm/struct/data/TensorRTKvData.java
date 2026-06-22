package com.erling.trt.illm.struct.data;

import com.erling.trt.core.en.TensorRTDataEnum;
import com.erling.trt.core.struct.cuda.TensorRTCudaAddress;
import com.erling.trt.core.struct.rt.TensorRtShape;

public class TensorRTKvData<T> {

    public T data;

    public TensorRTKvData<T> setData(T data){
        this.data = data;
        return this;
    }
    public TensorRTKvData<T> setType(int type) {
        this.type = type;
        return this;
    }

    public int type;
    public TensorRtShape pastKvShape;
    public TensorRtShape presentKvShape;

    public TensorRTKvData<T> setPresentKvShape(TensorRtShape presentKvShape) {
        this.presentKvShape = presentKvShape;
        return this;
    }

    public TensorRTKvData<T> setPastKvShape(TensorRtShape pastKvShape) {
        this.pastKvShape = pastKvShape;
        return this;
    }

    public TensorRTKvData<T> setPastKvAddress(TensorRTCudaAddress pastKvAddress) {
        this.pastKvAddress = pastKvAddress;
        return this;
    }

    public TensorRTKvData<T> setPresentKvAddress(TensorRTCudaAddress presentKvAddress) {
        this.presentKvAddress = presentKvAddress;
        return this;
    }

    public TensorRTKvData<T> setDataType(TensorRTDataEnum dataType) {
        this.dataType = dataType;
        return this;
    }

    public TensorRTCudaAddress pastKvAddress;
    public TensorRTCudaAddress presentKvAddress;

    public TensorRTDataEnum dataType;
}
