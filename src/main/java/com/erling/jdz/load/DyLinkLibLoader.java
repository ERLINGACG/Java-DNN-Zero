package com.erling.jdz.load;

import com.erling.jdz.load.ann.*;
import com.erling.jdz.load.cac.Cache_L1;
import com.erling.jdz.load.cac.Cache_L2;
import com.erling.jdz.uitls.run.TaskRun;
import com.sun.jna.Library;
import com.sun.jna.Native;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import static jdk.nio.zipfs.ZipFileAttributeView.AttrID.method;

public class DyLinkLibLoader {

    private static final Logger logger = LogManager.getLogger(DyLinkLibLoader.class);
    @SuppressWarnings("unchecked")
    public static <T extends Library> T load(Class<? extends Library> libraryClass){
        DyLinkLib dyLinkLib=libraryClass.getAnnotation(DyLinkLib.class);

        if(dyLinkLib==null || dyLinkLib.path().isEmpty()){
            throw new IllegalArgumentException("DyLinkLib annotation not found");
        }
        try {
            return (T) Native.load(
                    (dyLinkLib.path()),
                    libraryClass
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load library: " + dyLinkLib.path(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Library> T load(Class<? extends Library> libraryClass,String path){
        try {
            return (T) Native.load(
                    (path),
                    libraryClass
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load library: " + path, e);
        }
    }

   public static Map<Class<?>, Cache_L1> CACHE_L1 = new ConcurrentHashMap<>();

   public static Map<Class<?>, Cache_L2> CACHE_L2 = new ConcurrentHashMap<>();

   public static Map<Cache_L2, Library> CACHE_L3 = new ConcurrentHashMap<>();

    public static void load(Object targetObj,Class<?> targetClass,Class<? extends Library> lib) throws IllegalAccessException {

        Cache_L1 leve1 = CACHE_L1.get(targetObj.getClass());
        if (leve1 == null) {
            leve1 = new Cache_L1(
                    targetObj.getClass(),
                    targetObj.getClass().getDeclaredFields()
            );
            System.out.println("load 1");
            CACHE_L1.put(targetObj.getClass(), leve1);
        }
        var inject_2 = (TaskRun) (arg) -> {
            for (var field : (Field[]) arg[0]) { //arg[0] 是 fields1 ,字段列表
                field.setAccessible(true);
                if (field.getType().isAssignableFrom(arg[2].getClass())) { // 判断 injectInstance是否可以赋值给f
                    field.set(arg[1], arg[2]);     // arg[1] 是 targetObj, arg[2] 是 injectInstance，
                }
            }
        };

        var inject_3 = (TaskRun) (arg) -> {
            for (var method : (Method[]) arg[0]) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(init.class)) {
                    try {
                        method.invoke(arg[1]);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(
                                "Failed to invoke init method: " + method.getName(), e
                        );
                    }
                }
            }
        };

        Cache_L1 finalLeve = leve1;
        var inject_1 = (TaskRun) (objects) -> {
            for (var f1 : finalLeve.fields1()) {
                f1.setAccessible(true);
                var dyLinkLib = f1.getAnnotation(DyLinkLib.class);
                var configPath = f1.getAnnotation(ConfigPath.class);
                var noConfig = f1.getAnnotation(NoConfig.class);
                var obj = f1.get(targetObj);
                if (obj == null && configPath != null && noConfig == null) {
                    try {
                        Constructor<?> constructor = f1.getType().getConstructor(String.class);
                        obj = constructor.newInstance(configPath.value());
                        f1.set(targetObj, obj);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(
                                "Failed to invoke init method : " + e.getMessage(), e
                        );
                    }
                } else if (obj == null && configPath == null && noConfig != null) {
                    try {
                        Constructor<?> constructor = f1.getType().getConstructor();
                        obj = constructor.newInstance();
                        f1.set(targetObj, obj);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(
                                "Failed to invoke init method : " + e.getMessage(), e
                        );
                    }
                }
                if (obj != null && targetClass.isAssignableFrom(obj.getClass())) {
                    var leve2 = CACHE_L2.get(obj.getClass());
                    if (leve2 == null) {
                        leve2 = new Cache_L2(
                                obj.getClass(),
                                obj.getClass().getDeclaredFields(),
                                obj.getClass().getDeclaredMethods()
                        );
                        System.out.println("load 2");
                        CACHE_L2.put(leve2.aClass2(), leve2);
                    }
                    var INSTANCE = CACHE_L3.get(leve2);
                    if (INSTANCE == null) {
                        INSTANCE = Native.load(dyLinkLib.path(), lib);
                        System.out.println("load 3");
                        CACHE_L3.put(leve2, INSTANCE);
                    }
                    inject_2.run(leve2.fields2(), obj, INSTANCE);
                    inject_3.run(leve2.methods2(), obj);
                }
            }
        };
        inject_1.run();
    }

    public static void load(Object targetObj) throws IllegalAccessException {
        Cache_L1 leve1 = CACHE_L1.get(targetObj.getClass());
        if (leve1 == null) {
            leve1 = new Cache_L1(
                    targetObj.getClass(),
                    targetObj.getClass().getDeclaredFields()
            );
            CACHE_L1.put(targetObj.getClass(), leve1);
        }


        Cache_L1 finalLeve = leve1;

        var set_instance= (TaskRun)(arg)->{
            var filed_list = (Field[]) arg[0];
            var i_targetObj    = arg[1];
            var injectInstance = arg[2];
            for (var field : filed_list) { //arg[0] 是 fields1 ,字段列表
                field.setAccessible(true);
                if (field.getType().isAssignableFrom(injectInstance.getClass())) { // 判断 injectInstance是否可以赋值给f
                    field.set(i_targetObj, injectInstance);     // arg[1] 是 targetObj, arg[2] 是 injectInstance，
                }
            }
        };

        var set_init_method = (TaskRun)(arg)->{
                var method_list = (Method[]) arg[0];
                var i_targetObj =    arg[1];
                for (var method : method_list) { //arg[0] 是 methods1 ,方法列表
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(init.class)) {
                        try {
                            method.invoke(i_targetObj);
                        } catch (Exception e) {
                            logger.error("load dyLinkLib error: {}", e.getMessage(), e);
                        }
                    }
                }
        };

        var init_obj = (TaskRun) (arg) -> {
            for(var field_1: finalLeve.fields1()){
                field_1.setAccessible(true);
                var dyLinkLib = field_1.getAnnotation(DyLinkLib.class);
                var configPath = field_1.getAnnotation(ConfigPath.class);
                var noConfig = field_1.getAnnotation(NoConfig.class);
                var mapping = field_1.getAnnotation(Mapping.class);
                var obj = field_1.get(targetObj);
                if (obj == null && configPath != null && noConfig == null) {
                    try{
                        Constructor<?> constructor = field_1.getType().getConstructor(String.class);
                        obj = constructor.newInstance(configPath.value());
                        logger.debug("set obj");
                        field_1.set(targetObj, obj);
                    }catch (Exception e){
                        logger.error("load dyLinkLib error: {}", e.getMessage(), e);
                    }
                }else if (obj == null && configPath == null && noConfig != null) {
                    try{
                        Constructor<?> constructor = field_1.getType().getConstructor();
                        obj = constructor.newInstance();
                        field_1.set(targetObj, obj);
                    }catch (Exception e){
                        logger.error("load dyLinkLib error: {}", e.getMessage(), e);
                    }
                }

                if (obj != null && mapping != null
//                        && obj.getClass().isAssignableFrom(obj.getClass())
                ){
                    var leve2 = CACHE_L2.get(obj.getClass());
                    if (leve2 == null) {
                        leve2 = new Cache_L2(
                                obj.getClass(),
                                obj.getClass().getDeclaredFields(),
                                obj.getClass().getDeclaredMethods()
                        );
                        CACHE_L2.put(leve2.aClass2(), leve2);
                    }
                    var INSTANCE = CACHE_L3.get(leve2);
                    if (INSTANCE == null) {
                        INSTANCE = Native.load(dyLinkLib.path(), mapping.value());
                        CACHE_L3.put(leve2, INSTANCE);
                    }
                    set_instance.run(leve2.fields2(), obj, INSTANCE);
                    set_init_method.run(leve2.methods2(), obj);
                }
            }
        };
        init_obj.run();
    }



}
