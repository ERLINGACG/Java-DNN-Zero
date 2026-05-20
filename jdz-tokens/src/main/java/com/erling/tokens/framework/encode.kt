package com.erling.tokens.framework

import com.erling.tokens.struct.TokenizerIDS
import com.erling.tokens.struct.TokenizerLoader
import java.lang.foreign.Arena

fun encode(
    tokenizerLoader: TokenizerLoader,
    tokensFramework: TokensFramework,
    ids: TokenizerIDS,text: String
): IntArray {
    tokensFramework.encode(
        tokenizerLoader.memorySegment,text,
        ids.memorySegment
    )
    return ids.ids.getForIntArrayArray(ids.len.get().toLong())
}

