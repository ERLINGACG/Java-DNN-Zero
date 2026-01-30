package com.erling.jdz.cv.framework.face.param;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

//@Structure.FieldOrder({"input","blob"})
public class BaseParam extends Structure {

    public Pointer input;
    public Pointer blob;

    public BaseParam() {
        super(Structure.ALIGN_NONE);
    }
    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("input", "blob");
    }

}
