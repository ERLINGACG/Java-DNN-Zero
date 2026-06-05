# JDnn-Zero

JDnn-Zero 是一个面向 Java/JVM 的本地深度学习调用框架，主要用于在 Java 中接入 C/C++ 动态库，并封装 LLM、ONNX Runtime、TensorRT、OpenCV/YOLO、Tokenizer 等推理能力。

项目当前处于开发阶段，代码中同时存在 JNA 与 Java FFM 两类本地调用方式；部分示例依赖本机动态库、模型文件和配置路径，需要按自己的环境调整后运行。

## 核心特性

- **本地库自动加载**：通过注解与加载器将 Java 字段注入为 JNA/FFM 框架实例。
- **运行环境配置**：从 TOML 配置中读取并加载 OpenCV、llama.cpp、ONNX Runtime、TensorRT 等动态库。
- **LLM 推理封装**：支持基于 llama.cpp/GGUF 的模型加载、流式输出、批处理、函数调用和 grammar 约束。
- **ONNX Runtime 封装**：包含 LLM、Embedding 等 ONNX Runtime 推理模块。
- **TensorRT/RAG 封装**：包含 TensorRT 基础接口与 RAG 相关实验模块。
- **CV 推理封装**：提供 OpenCV YOLO 目标检测接口。
- **Tokenizer 支持**：提供分词器加载、文本转 token、token 转文本等能力。

## 项目结构

```text
JDnnZero/
├── build.gradle                 # 根 Gradle 配置
├── settings.gradle              # 多模块声明与 JVM 版本
├── clibconf/                    # 旧版/示例本地库与模型配置
├── jdz-core/                    # 核心加载器、注解、FFM/JNA 基础设施
├── jdz-cv/                      # OpenCV/YOLO 相关封装
├── jdz-llama.cpp/               # llama.cpp/GGUF LLM 封装
├── jdz-ort-core/                # ONNX Runtime 基础封装
├── jdz-ort-llm/                 # ONNX Runtime LLM 封装
├── jdz-ort-emd/                 # ONNX Runtime Embedding 封装
├── jdz-plugins/                 # 插件/扩展模块
├── jdz-test/                    # 集成测试与调用示例
├── jdz-tokens/                  # Tokenizer 封装
├── jdz-trt-core/                # TensorRT 基础封装
├── jdz-trt-rag/                 # TensorRT RAG 相关封装
└── src/                         # 早期代码与测试保留目录
```

## 模块说明

| 模块 | 说明 |
| --- | --- |
| `jdz-core` | 项目核心模块，包含 `@JdzFramework`、`@JdzFrameFFM`、`@DyLinkLibInf` 等注解，JNA/FFM 加载器，运行环境加载，以及 C/C++ 结构体映射辅助类。 |
| `jdz-cv` | OpenCV/YOLO 推理封装，典型入口为 `CV_YoloFrameWork`，通过配置文件创建 YOLO 框架实例并执行检测。 |
| `jdz-llama.cpp` | llama.cpp 相关封装，包含 GGUF 模型调用、流式生成、批处理、function calling、grammar、agent 等能力。 |
| `jdz-ort-core` | ONNX Runtime 公共基础模块。 |
| `jdz-ort-llm` | ONNX Runtime LLM 调用封装，测试示例中结合 tokenizer 进行推理。 |
| `jdz-ort-emd` | ONNX Runtime Embedding 调用封装。 |
| `jdz-tokens` | Tokenizer 动态库封装，提供 tokenizer 加载和 token/id 转换能力。 |
| `jdz-trt-core` | TensorRT 基础接口与结构体封装。 |
| `jdz-trt-rag` | TensorRT RAG 相关实验/封装模块。 |
| `jdz-plugins` | 插件扩展模块。 |
| `jdz-test` | 集成测试、FFM/JNA 加载示例、LLM/CV/ONNX/TensorRT 调用示例。 |

## 环境要求

- **JDK 21**：`settings.gradle` 中通过 `gradle.ext.jvm_version = 21` 指定。
- **Gradle Wrapper**：推荐使用项目自带的 `gradlew` / `gradlew.bat`。
- **Windows 环境**：当前配置中的动态库路径以 Windows DLL 为主。
- **本地 C/C++ 动态库**：需要自行准备 OpenCV、llama.cpp、ONNX Runtime、TensorRT、Tokenizer 等对应动态库。
- **模型文件**：需要自行准备 GGUF、ONNX、TensorRT engine 等模型文件，并修改配置路径。

部分模块使用 Java FFM 和预览特性，构建脚本已为相关模块添加 `--enable-preview`。

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd JDnnZero
```

### 2. 配置动态库

根据实际路径修改动态库配置。示例配置位于：

```text
clibconf/libconfig.toml
```

示例：

```toml
ENV=[
    "D:/libs/opencv/bin/opencv_world4120.dll",
    "D:/libs/llama.cpp/bin/ggml-base.dll",
    "D:/libs/llama.cpp/bin/ggml-cpu.dll",
    "D:/libs/llama.cpp/bin/ggml-cuda.dll",
    "D:/libs/llama.cpp/bin/ggml.dll",
    "D:/libs/llama.cpp/bin/llama.dll"
]
```

加载逻辑会读取 `ENV` 列表并逐个调用 `System.load(...)`。

### 3. 配置模型

常见配置文件位于：

```text
clibconf/modelconf/
```

包括：

- `llm_gguf_config.json`：GGUF/llama.cpp 模型配置。
- `yolo_config.json`：YOLO 模型配置。
- `yunet_config.json`：YuNet 人脸检测配置。
- `arcface_config.json`：ArcFace 人脸识别配置。

示例 LLM 配置：

```json
{
  "env": "test",
  "test": {
    "model_path": "D:/models/qwen.gguf",
    "n_gpu_layers": 35,
    "use_mmap": true,
    "use_mlock": false,
    "n_ctx": 32000,
    "n_batch": 1024,
    "temp": 0.3,
    "top_k": 30,
    "top_p": 0.9,
    "is_grammar": false,
    "grammar_file": "./clibconf/modelconf/answer_grammar.gbnf"
  }
}
```

### 4. 构建项目

Windows：

```bat
gradlew.bat build
```

Linux/macOS：

```bash
./gradlew build
```

如果只是编译某个模块，可使用：

```bash
./gradlew :jdz-core:build
./gradlew :jdz-llama.cpp:build
./gradlew :jdz-test:test
```

## 典型用法

### 加载运行环境

```java
import com.erling.core.load.env.SetRunTimeEnv;

SetRunTimeEnv.SET.run("clibconf/libconfig.toml");
```

### JNA 框架加载

```java
import com.erling.core.load.ann.JdzFramework;
import com.erling.core.load.jna.DyLinkLibLoader;

public class Demo {
    @JdzFramework(
        rootPath = "./libconfig/share",
        name = "YourNativeLib",
        mapping = YourNativeMapping.class
    )
    private YourNativeMapping framework;

    public Demo() {
        DyLinkLibLoader.Load(this);
    }
}
```

### FFM 框架加载

```java
import com.erling.core.load.ann.JdzFrameFFM;
import com.erling.core.load.ffm.DyLinkLibLoaderForFFM;
import com.erling.core.load.ffm.api.cpp.hook.SetConfig;

public class Demo {
    @JdzFrameFFM(
        rootPath = "./libconfig/share",
        name = "YourNativeLib",
        useMappingConfig = false
    )
    @SetConfig(args = {"./libconfig/your_config.json"})
    private YourFramework framework;

    public Demo() {
        DyLinkLibLoaderForFFM.load(this);
    }
}
```

### YOLO 检测

```java
import com.erling.opencv.yolo.framework.CV_YoloFrameWork;
import com.erling.opencv.yolo.sturct.YoloOutput;

CV_YoloFrameWork yolo = new CV_YoloFrameWork("./clibconf/modelconf/yolo_config.json");
yolo.init_model();

YoloOutput output = new YoloOutput();
yolo.detect(imageBytes, output);

yolo.destroy();
```

实际使用可参考 `jdz-test/src/test/java/com/erling/test/cv/CVYoloTest.java`。

## 测试

运行全部测试：

```bash
./gradlew test
```

运行单个模块测试：

```bash
./gradlew :jdz-test:test
```

注意：很多测试是集成测试，依赖本机动态库、模型文件和配置路径。如果路径未配置或模型不存在，测试会失败。

## 配置说明

### 动态库配置

`clibconf/libconfig.toml` 使用 `ENV` 数组声明 DLL 加载顺序。依赖链中的基础库应放在前面，例如 OpenCV、`ggml-base.dll`、`ggml-cpu.dll`、`ggml-cuda.dll`、`ggml.dll`、`llama.dll`。

### 模型配置

模型配置通常使用如下结构：

```json
{
  "env": "test1",
  "test1": {
    "model_path": "./path/to/model",
    "input_height": 640,
    "input_width": 640
  }
}
```

其中 `env` 表示当前启用的配置名称，具体参数由不同模块读取。

## 主要依赖

| 依赖 | 用途 |
| --- | --- |
| JNA `5.14.0` | 调用 C/C++ 动态库。 |
| Kotlin JVM | Kotlin 源码与 Java 混合开发。 |
| Kotlin Serialization | JSON 序列化/反序列化。 |
| TOML4J `0.7.2` | 读取 TOML 配置。 |
| JUnit 5 | 单元测试与集成测试。 |
| Lombok | 简化部分 Java POJO/链式调用代码。 |

## 注意事项

- 当前仓库内的配置路径包含较多本机绝对路径，首次运行前需要改为自己的路径。
- 本项目大量依赖本地 C/C++ 动态库，Java 编译成功不代表推理示例可以直接运行。
- 使用 GPU/CUDA/TensorRT 时，需要确保驱动、CUDA、cuDNN、TensorRT 与动态库版本匹配。
- 运行 LLM 或大模型示例时，请确认显存/内存足够。
- 如果遇到 `UnsatisfiedLinkError`，优先检查 DLL 路径、加载顺序和依赖库是否缺失。
- 如果遇到 FFM 相关错误，请确认使用 JDK 21 并启用了预览参数。

## 许可证

待定

[//]: # (仓库中暂未看到独立许可证文件。请在发布或商用前补充明确的 LICENSE 文件。)
