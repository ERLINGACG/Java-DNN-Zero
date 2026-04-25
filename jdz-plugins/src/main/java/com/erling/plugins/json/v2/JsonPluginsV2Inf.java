package com.erling.plugins.json.v2;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface JsonPluginsV2Inf extends Library {

      interface JsonNodeInfoCallback extends Callback {
          void invoke(JsonNodeInfo json_node_info);
      }

      int BuildJsonData(JsonData json_data, String json_str);

      int BuildJsonData(JsonData json_data, Pointer json_str);


      int PostToKey(JsonData json_data, Pointer key,int type);


      int GetValue(JsonData data,JsonNodeInfo json_node_info);




}
