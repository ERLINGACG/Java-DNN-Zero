package com.erling.jdz.uitls.conf;

import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class ReadTomlConfig {

   static public List<String> readTomlConfig(String filePath,String key){
       Toml toml = new Toml().read(new File(filePath));
       return toml.getList(key);
    }
}
