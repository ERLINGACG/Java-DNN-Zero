package com.erling.llama.llm.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import lombok.Getter
import lombok.Setter

/**
 * <p>OpenAI API请求对象示例：</p>
 * <pre>
 * <code>
 * {
 *   "messages": [
 *     {
 *       "content": "北京和上海哪个城市天气更好？先查一下天气。",
 *       "role": "user"
 *     }
 *   ],
 *   "model": "你的模型名称",
 *   "stream": false,
 *   "temperature": 0.7,
 *   "tools": [
 *     {
 *       "type": "function",
 *       "function": {
 *         "name": "get_weather",
 *         "description": "根据城市名称获取该地的实时天气情况。",
 *         "parameters": {
 *           "properties": {
 *             "city": {
 *               "type": "string"
 *             }
 *           },
 *           "required": ["city"],
 *           "type": "object"
 *         }
 *       }
 *     },
 *     {
 *       "type": "function",
 *       "function": {
 *         "name": "calculate",
 *         "description": "安全地计算一个数学表达式的值。例如，输入'2+3'，返回'5'。",
 *         "parameters": {
 *           "properties": {
 *             "expression": {
 *               "type": "string"
 *             }
 *           },
 *           "required": ["expression"],
 *           "type": "object"
 *         }
 *       }
 *     }
 *   ]
 * }
 * </code>
 * </pre>
 */

@Serializable
@Getter
@Setter
data class OpenAIRequestObj(
    val messages: List<Message>?=null,
    val model: String?=null,
    val stream: Boolean?=null,
    val temperature: Double?=null,
    val tools: List<Tool>?=null,
    val stream_options: Map<String, Boolean>?=null,
)

@Serializable
data class Tool(
    val type: String?=null,
    val function: Function? = null
)

@Serializable
data class Function(
    val name: String?=null,
    val description: String?=null,
    val parameters: Parameters? = null
)

@Serializable
data class Parameters(
    val type: String?=null,
    val properties: Map<String, Property>?=null,
    val required: List<String>?=null
)

@Serializable
data class Property(
    val type: String?=null,
    val description: String?=null
)