# JDnn-Zero

JDnn-Zero 是一个功能强大的Java深度学习框架，提供了对大型语言模型(LLM)和计算机视觉(CV)任务的支持。该框架通过JNA技术调用C/C++底层库，实现了高效的模型推理能力。

## 项目结构

```
JDnnZero/
├── jdz-core/          # 核心库，提供基础功能
├── jdz-cv/            # 计算机视觉模块
├── jdz-llama.cpp/     # LLM模块，基于llama.cpp
├── clibconf/          # 配置文件和模型目录
│   ├── lib/           # 动态链接库
│   ├── model/         # 模型文件
│   └── modelconf/     # 模型配置文件
└── src/               # 源代码
    ├── main/java/com/erling/jdz/
    │   ├── base/      # 基础类
    │   ├── cv/        # 计算机视觉相关代码
    │   ├── llm/       # 语言模型相关代码
    │   ├── load/      # 动态库加载相关代码
    │   └── uitls/     # 工具类
    └── test/          # 测试代码
```

## 核心功能

### 1. 大型语言模型(LLM)支持

- **模型格式**：支持GGUF格式模型
- **流式输出**：支持模型推理的流式输出
- **函数调用**：支持模型调用Java方法
- **批量处理**：支持批量文本处理
- **嵌入式向量**：支持生成文本的嵌入式向量
- **模型支持**：支持GLM4、Qwen等多种模型

### 2. 计算机视觉(CV)支持

- **目标检测**：基于YOLO模型的目标检测
- **人脸识别**：基于YuNet和ArcFace模型的人脸识别
- **特征提取**：支持人脸特征提取

### 3. 动态库加载

- **自动加载**：通过注解自动加载动态链接库
- **配置管理**：支持通过TOML配置文件管理库路径
- **环境设置**：自动设置运行环境

## 技术栈

- **Java**：主要开发语言
- **JNA**：用于调用C/C++动态链接库
- **Gradle**：项目构建工具
- **ASM**：用于字节码操作
- **Lombok**：用于简化代码
- **Log4j**：用于日志记录
- **Jackson**：用于JSON处理
- **TOML4J**：用于读取配置文件

## 快速开始

### 环境要求

- Java 8 或更高版本
- Gradle 7.0 或更高版本
- Windows 操作系统（目前仅支持Windows）

### 安装

1. 克隆项目到本地

```bash
git clone <repository-url>
cd JDnnZero
```

2. 构建项目

```bash
./gradlew build
```

### 配置

1. 配置动态链接库路径

在 `clibconf/libconfig.toml` 文件中配置动态链接库路径：

```toml
[lib]
path = "path/to/GeneralDnnLib_Framework_Lib.dll"
```

2. 配置模型路径

在 `clibconf/modelconf/` 目录下配置模型相关参数：

- `llm_gguf_config.json`：LLM模型配置
- `yolo_config.json`：YOLO模型配置
- `yunet_config.json`：YuNet模型配置
- `arcface_config.json`：ArcFace模型配置



## 模块说明

### jdz-core

核心库，提供基础功能：
- 动态库加载
- 环境设置
- 配置文件读取

### jdz-cv

计算机视觉模块：
- YOLO目标检测
- YuNet人脸检测
- ArcFace人脸识别

### jdz-llama.cpp

LLM模块：
- GGUF模型加载和推理
- 流式输出
- 函数调用
- 批量处理
- 嵌入式向量生成

## 配置说明

### 动态库配置

在 `clibconf/libconfig.toml` 文件中配置：

```toml
[lib]
path = "path/to/GeneralDnnLib_FrameWork_Lib.dll"
```

### 模型配置

#### LLM模型配置

在 `clibconf/modelconf/llm_gguf_config.json` 文件中配置：

```json
{
  "model_path": "path/to/model.gguf",
  "n_ctx": 2048,
  "n_threads": 4
}
```

#### CV模型配置

在 `clibconf/modelconf/` 目录下配置相应的模型参数。

## 依赖项

| 依赖项 | 版本 | 用途 |
|-------|------|------|
| JNA | 5.14.0 | 调用C/C++动态链接库 |
| TOML4J | 0.7.2 | 读取TOML配置文件 |
| ASM | 9.7 | 字节码操作 |
| Lombok | 1.18.30 | 简化代码 |
| Log4j | 2.20.0 | 日志记录 |
| Jackson | 2.17.2 | JSON处理 |

## 开发指南

### 添加新功能

1. 在相应模块中创建新的类
2. 实现相应的接口
3. 添加测试代码
4. 构建并测试

### 测试

运行测试：

```bash
./gradlew test
```

## 注意事项

- 目前仅支持Windows操作系统
- 需要正确配置动态链接库路径
- 需要正确配置模型路径和参数
- 大型模型可能需要较多的内存
- README.md文件AI写的，部分内容不全

## 许可证

[MIT License](LICENSE)

## 联系方式

如有问题或建议，请联系项目维护者。