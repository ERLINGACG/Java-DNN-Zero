package com.erling.jdz.cv.framework.face.param;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Structure.FieldOrder({"faces","rects"})
public class FaceParam extends BaseParam {

     public  Pointer faces;
     public  Pointer rects;

     public FaceParam() {
          super();
     }

     @Override
     protected List<String> getFieldOrder() {
          // ✅ 修复点1【核心】：把父类的不可变List，包装成 可修改的ArrayList
          List<String> fieldList = new ArrayList<>(super.getFieldOrder());
          // ✅ 现在执行addAll完全没问题，不会抛异常
          fieldList.addAll(Arrays.asList("faces", "rects"));
          // 返回完整4个字段：input → blob → faces → rects，顺序和C++完全一致
          return fieldList;
     }
}
