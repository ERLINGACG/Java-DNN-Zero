package com.erling.llama.llm.prompt
//    load: control token: 151661 '<|fim_suffix|>' is not marked as EOG
//    load: control token: 151649 '<|box_end|>' is not marked as EOG
//    load: control token: 151647 '<|object_ref_end|>' is not marked as EOG
//    load: control token: 151654 '<|vision_pad|>' is not marked as EOG
//    load: control token: 151659 '<|fim_prefix|>' is not marked as EOG
//    load: control token: 151648 '<|box_start|>' is not marked as EOG
//    load: control-looking token: 128247 '</s>' was not control-type; this is probably a bug in the model. its type will be overridden
//    load: control token: 151644 '<|im_start|>' is not marked as EOG
//    load: control token: 151646 '<|object_ref_start|>' is not marked as EOG
//    load: control token: 151650 '<|quad_start|>' is not marked as EOG
//    load: control token: 151651 '<|quad_end|>' is not marked as EOG
//    load: control token: 151652 '<|vision_start|>' is not marked as EOG
//    load: control token: 151653 '<|vision_end|>' is not marked as EOG
//    load: control token: 151655 '<|image_pad|>' is not marked as EOG
//    load: control token: 151656 '<|video_pad|>' is not marked as EOG
//    load: control token: 151660 '<|fim_middle|>' is not marked as EOG
//    load: printing all EOG tokens:
//    load:   - 128247 ('</s>')
//    load:   - 151643 ('<|endoftext|>')
//    load:   - 151645 ('<|im_end|>')
//    load:   - 151662 ('<|fim_pad|>')
//    load:   - 151663 ('<|repo_name|>')
//    load:   - 151664 ('<|file_sep|>')
class Qwen3Template : IPromptTemplate {



    private var eogMap: Map<String, Int> = DEFAULT_EOG_MAP

    companion object {
        val DEFAULT_EOG_MAP = mapOf(
            "</s>" to 128247,           // 标准结束符
            "<|endoftext|>" to 151643,  // 备用结束符
            "<|im_end|>" to 151645,     // ChatML 格式的对话结束符
            "<|fim_pad|>" to 151662,    // FIM 填充符（非 EOG，但模型标记为 EOG）
            "<|repo_name|>" to 151663,
            "<|file_sep|>" to 151664
        )
    }

    override fun getEogMap(): Map<String, Int> = eogMap
    override fun getEndTokenID(): Int = eogMap["<|im_end|>"]?:-1


    override fun format(system: String, prompt: String): String {
        return StringBuffer().
            append("<|im_start|>system\n").
            append(system).
            append("\n<|im_end|>\n").
            append(prompt).
            append("\n<|im_start|>assistant\n").toString()

    }

    override fun eogMap(optIn: () -> Map<String, Int>) {
        eogMap = optIn()
    }


}
