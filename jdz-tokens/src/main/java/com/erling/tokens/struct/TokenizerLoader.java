package com.erling.tokens.struct;

import com.erling.core.load.ffm.api.cpp.struct.NativeStruct;
import com.erling.core.load.ffm.api.cpp.struct.field.C_POINTER;
import com.erling.tokens.framework.TokensFramework;
import com.erling.tokens.framework.TokensFrameworkInf;

import java.lang.foreign.Arena;
import java.lang.reflect.Array;
import java.util.Arrays;

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


    private TokensFramework iFramework;
    
    /**
     * 构造函数
     * @param arena 内存区域，用于分配原生结构体内存
     */
    public TokenizerLoader(Arena arena) {
        super(TokenizerLoader.class, arena);
    }

    public TokenizerLoader setIFramework(TokensFramework iFramework) {
        this.iFramework = iFramework;
        this.iFramework.loadTokenizer(this.getMemorySegment());
        return this;
    }



    public int[] encodeForInt(TokenizerIDS ids, String str){
        iFramework.encode(this.getMemorySegment(), str, ids.getMemorySegment());
        return ids.ids.getForIntArrayArray(ids.len.get());
    }

    public long[] encodeForLong(TokenizerIDS ids, String str){
        return Arrays.stream(this.encodeForInt(ids, str)).mapToLong(Long::valueOf).toArray();
    }

    public String decode(TokenizerTokens token, int tokens){
        iFramework.decodeToken(this.getMemorySegment(), tokens, token.getMemorySegment());
        return token.tokens.getForString();
    }

    public int encodeIdsFree(TokenizerIDS ids){
        return iFramework.encodeIdsFree(ids.getMemorySegment());
    }
    public int decodeTokensFree(TokenizerTokens tokens){
        return iFramework.decodeTokensFree(tokens);
    }
}