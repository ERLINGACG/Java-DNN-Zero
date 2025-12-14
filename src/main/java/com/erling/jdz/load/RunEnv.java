package com.erling.jdz.load;

import com.erling.jdz.uitls.conf.ReadTomlConfig;
import lombok.val;

public enum RunEnv {
    SET_ENV(),;


    public void run(){}
    RunEnv(){
       val liblist = ReadTomlConfig.readTomlConfig("./clibconf/libconfig.toml","ENV");
       try {
           for(val key : liblist){
               if(!key.isEmpty()){
                   System.load(key);
                   System.out.println(key + " is loaded successfully");
               }
           }
       }catch (Exception e){
           System.out.println(e.getMessage());
       }

    }
}
