package com.erling.tokens.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;

import java.lang.foreign.Arena;

/**
 * TokenizerLoader 结构体的 JNI 映射
 * <p>
 * 对应 Rust 端的 #[repr(C)] pub struct TokenizerLoader { pub tokenizer: *mut Tokenizer, }
 * </p>
 *
 * @author erling
 * @since 1.0
 */
public class TokenizerLoader extends NativeStruct<TokenizerLoader> {

    /**
     * tokenizer 字段，对应 Rust 端的 Arc<Mutizer> 指针
     * <p>
     * 该字段用于存储指向 Rust Tokenizer 实例的原始指针
     * </p>
     */
    public C_POINTER tokenizer;
    
    /**
     * 构造函数
     * @param arena 内存区域，用于分配原生结构体内存
     */
    public TokenizerLoader(Arena arena) {
        super(TokenizerLoader.class, arena);
    }
}