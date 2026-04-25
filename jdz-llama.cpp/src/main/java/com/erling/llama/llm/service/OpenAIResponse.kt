package com.erling.llama.llm.service

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponseObj(
    val id: String? = null,
    val `object`: String? = null,
    val created: Long? = null,
    val model: String? = null,
    val choices: List<Choice>? = null,
    val usage: Usage? = null
)

@Serializable
data class Choice(
    val index: Int? = null,
    val message: Message? = null,
    val finish_reason: String? = null   // "tool_calls" 或 "stop"
)

@Serializable
data class Message(
    val role: String? = null,
    val content: String? = null,
    val tool_calls: List<ToolCall>? = null   // 工具调用时使用
)

@Serializable
data class ToolCall(
    val id: String,
    val type: String,              // "function"
    val function: FunctionCall
)

@Serializable
data class FunctionCall(
    val name: String,
    val arguments: String          // JSON 字符串
)

@Serializable
data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)