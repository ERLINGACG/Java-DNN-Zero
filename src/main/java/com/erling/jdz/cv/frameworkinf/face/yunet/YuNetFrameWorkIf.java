package com.erling.jdz.cv.frameworkinf.face.yunet;

import com.erling.jdz.cv.framework.face.feature.FeatureOutput;
import com.erling.jdz.cv.framework.face.yunet.YuNetOutput;
import com.erling.jdz.cv.framework.face.yunet.YunetParam;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * YuNet 人脸检测框架核心 API 声明（C++ 层）：
 * <pre>
 * // 1. 创建 YuNet 框架实例
 * GDLZ_CORE_API yunet::CV_YuNetFramework* CreateYuNetFramework(const char* conf_path);
 *
 * // 2. 销毁 YuNet 框架实例
 * GDLZ_CORE_API void DestroyYuNetFramework(const yunet::CV_YuNetFramework* framework);
 * </pre>
 */
public interface YuNetFrameWorkIf extends Library {
      Pointer CreateYuNetFramework(String conf_path);
      void DestroyYuNetFramework(Pointer framework);
      void YuNetDetection(
              Pointer framework, int size , byte[] data,
              YuNetOutput output
      );
      void CV_YuNetDetection_FeatureVec_Arc(
              Pointer framework,
              Pointer Arcframework, int size , byte[] data,
              YuNetOutput   output0,
              FeatureOutput output1
      );

}
