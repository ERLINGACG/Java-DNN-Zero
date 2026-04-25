package com.erling.llama.llm.service


import kotlinx.serialization.Serializable
import lombok.Getter


@Getter
data class OpenAISseResponseObj(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<IChoice>

)

data class IChoice(
    val index: Int,
    val delta: Map<String, String>,
    val finish_reason: String?
)
