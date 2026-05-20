package com.erling.tokens.framework;

import java.lang.foreign.MemorySegment;

/**
 * TokensFramework 接口定义
 * <p>
 * 对应 Rust 端的 Tokenizer 加载和管理功能
 * </p>
 *
 * @author erling
 * @since 1.0
 */
public interface TokensFrameworkInf {

    /**
     * 加载 Tokenizer
     * <p>
     * 对应 Rust 函数：gdlz_tokens_loader
     * </p>
     *
     * @param tokenizerLoader TokenizerLoader 的内存段
     * @param path tokenizer 文件路径的内存段
     * @return 成功时返回 0，失败时返回错误代码
     */
    int gdlz_tokens_loader(MemorySegment tokenizerLoader, MemorySegment path);


    int gdlz_tokens_encode(MemorySegment tokenizerLoader, MemorySegment memorySegment, MemorySegment ids);

    int gdlz_tokens_decode_tokens(MemorySegment tokenizerLoader, MemorySegment ids, MemorySegment tokens);

    int gdlz_tokens_decode_token(MemorySegment tokenizerLoader, int id, MemorySegment token);
}