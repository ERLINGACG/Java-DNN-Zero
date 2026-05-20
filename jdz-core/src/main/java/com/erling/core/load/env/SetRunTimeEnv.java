package com.erling.core.load.env;

public enum SetRunTimeEnv {
    SET,
    UNSET,;
    public void run(){}

    public void run(String path){
        var env = ReadTomKt.readToml(path,"ENV");
        try {
            for(var key : env){
                if(!key.isEmpty()){
                    System.load(key);
                    System.out.println(key + " is loaded successfully");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    SetRunTimeEnv(){
         var env = ReadTomKt.readToml("libconfig/libconfig.toml","ENV");
        try {
            for(var key : env){
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
