package com.erling.test.plugins.json.ffm;

import java.lang.foreign.MemorySegment;

public interface JPluginsCppInf {
     int BuildJsonData(MemorySegment jsonData, MemorySegment jsonStr);

     int PostToKey(MemorySegment jsonData, MemorySegment key,int type);

     int GetValue(MemorySegment jsonData, MemorySegment info);

     void  PrintJsonNodeInfo();
     void At(MemorySegment jsonData);
}
