package com.erling.llama.llm.prompt

interface IPromptTemplate {

    fun eogMap(optIn: ()-> Map<String, Int>)

    fun getEogMap(): Map<String, Int>

    fun getEndTokenID(): Int

    fun format(system: String, prompt: String): String
}