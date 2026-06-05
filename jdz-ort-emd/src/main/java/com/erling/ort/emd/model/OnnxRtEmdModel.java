package com.erling.ort.emd.model;

import com.erling.ort.core.struct.OnnxRtShape;
import com.erling.ort.emd.exce.OrtEmdFramIsNull;
import com.erling.ort.emd.framework.ffm.OnnxRtEmdFramework;
import com.erling.ort.emd.struct.OnnxRtEmdCtx;
import com.erling.ort.emd.struct.OnnxRtEmdData;

import java.util.List;
import java.util.function.Supplier;

public class OnnxRtEmdModel {



    private OnnxRtEmdFramework emdFramework;

    public OnnxRtEmdModel setEmdFramework(OnnxRtEmdFramework emdFramework) {
        this.emdFramework = emdFramework;
        return this;
    }

    public OnnxRtEmdFramework emdFramework() {
        return emdFramework;
    }

    public OnnxRtEmdModel setDims(OnnxRtEmdCtx ctx,
                                  Supplier<List<OnnxRtShape>> shapeArraySupplier
    ) {
      if (this.emdFramework==null) {
        throw new OrtEmdFramIsNull("emdFramework is null");
      }
      var shapeArray = shapeArraySupplier.get();
      return OnnxEmdModelOperationKt.setDims(this,shapeArray,ctx);
    }

    public OnnxRtEmdModel forward(OnnxRtEmdCtx ctx, OnnxRtEmdData data) {
        if (this.emdFramework==null) {
            throw new OrtEmdFramIsNull("emdFramework is null");
        }
        return OnnxEmdModelOperationKt.forward(this,ctx,data);
    }

}
