package com.erling.tokens.framework;

import com.erling.core.load.ffm.api.cpp.clasz.NativeClass;
import com.erling.core.load.ffm.api.cpp.hook.AutoInit;
import com.erling.core.load.ffm.api.cpp.hook.CreatProxy;
import com.erling.core.load.ffm.api.cpp.hook.InitFunction;
import com.erling.tokens.struct.TokenizerTokens;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.util.Map;

/**
 * TokensFramework 框架类
 * <p>
 * 提供 Tokenizer 加载和管理的功能
 * </p>
 *
 * @author erling
 * @since 1.0
 */
public class TokensFramework implements NativeClass {

    /**
     * 函数映射表，存储 JNI 函数句柄
     */
    Map<String, MethodHandle> functionMap;


    String configPath;


    /**
     * TokensFramework 接口代理
     */
    @CreatProxy(mappingClass = TokensFrameworkInf.class)
    public TokensFrameworkInf frameworkInf;



    @Override
    @InitFunction
    public void setFunction(Map<String, MethodHandle> functionMap) {
        this.functionMap = functionMap;
    }

    @Override
    public MethodHandle getFunction(String name) {
        return functionMap.get(name);
    }

    @Override
    public Map<String, MethodHandle> getFunctionMap() {
        return functionMap;
    }

    @AutoInit
    public void AutoInit(String path) {
        this.configPath = path;
    }

    /**
     * 加载 Tokenizer
     *
     * @param tokenizerLoader TokenizerLoader 实例
     * @return 操作结果代码
     */
    public int loadTokenizer(MemorySegment tokenizerLoader) {
        try(Arena arena = Arena.ofShared()){
            return frameworkInf.gdlz_tokens_loader(tokenizerLoader, arena.allocateUtf8String(configPath));
        }
    }
    public int loadTokenizer(MemorySegment tokenizerLoader, String path) {
        try(Arena arena = Arena.ofShared()){
            return frameworkInf.gdlz_tokens_loader(tokenizerLoader, arena.allocateUtf8String(path));
        }
    }

    public int encode(MemorySegment tokenizerLoader, String text, MemorySegment ids) {
        try(Arena arena = Arena.ofShared()){
            return frameworkInf.gdlz_tokens_encode(tokenizerLoader, arena.allocateUtf8String(text), ids);
        }
    }

    public int decodeTokens(MemorySegment tokenizerLoader, MemorySegment ids, MemorySegment tokens) {
        try(Arena arena = Arena.ofShared()){
            return frameworkInf.gdlz_tokens_decode_tokens(tokenizerLoader, ids, tokens);
        }
    }

    public int decodeToken(MemorySegment tokenizerLoader, int id, MemorySegment token) {
        try(Arena arena = Arena.ofShared()){
            return frameworkInf.gdlz_tokens_decode_token(tokenizerLoader, id, token);
        }
    }
    public int encodeIdsFree(MemorySegment ids) {
        try(Arena arena = Arena.ofShared()){
            return frameworkInf.gdlz_tokens_encode_ids_free(ids);
        }
    }
    public int decodeTokensFree(TokenizerTokens tokens) {
            return frameworkInf.gdlz_tokens_decode_tokens_free(tokens.getMemorySegment());
    }
}
