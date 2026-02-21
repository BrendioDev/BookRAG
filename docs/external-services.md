# External Services Reference

BookRAG depends on several external services for inference. This document maps each service, its supported backends, and how to configure them.

---

## Service Overview

| Service | Purpose | Backends Supported | Cloud-Ready? |
|---|---|---|---|
| **LLM** | Text generation, entity extraction, planning | `openai` (OpenAI-compatible API) | Yes |
| **VLM** | Vision-language tasks (image description) | `gpt` (OpenAI-compatible API) | Yes |
| **Text Embedding** | Graph entity embedding, VDB text indexing | `local`, `ollama`, `openai` | Yes |
| **Text Reranker** | Entity resolution scoring, retrieval reranking | `local`, `vllm`, `jina` | Yes (via Jina) |
| **MM Embedding (GME)** | Multimodal VDB indexing (text + image) | `local` only (GPU singleton) | No (local GPU only) |
| **MinerU** | PDF extraction | `vlm-sglang-client` | No (local service) |

---

## 1. LLM (`Core/provider/llm.py`)

**Config:** `llm` section in system YAML / `LLMConfig`

Uses the OpenAI SDK — any OpenAI-compatible endpoint works (vLLM, Ollama with OpenAI mode, actual OpenAI API).

### Local (vLLM)
```yaml
llm:
  model_name: Qwen/Qwen3-8B-AWQ
  api_key: openai
  api_base: http://localhost:8003/v1
  backend: openai
```

### Cloud (OpenAI)
```yaml
llm:
  model_name: gpt-4o-mini
  api_key: ${OPENAI_API_KEY}
  api_base: https://api.openai.com/v1
  backend: openai
```

---

## 2. VLM (`Core/provider/vlm.py`)

**Config:** `vlm` section in system YAML / `VLMConfig`

Also uses OpenAI-compatible API with vision support.

### Local (vLLM / SGLang)
```yaml
vlm:
  model_name: Qwen2-5-VL
  api_key: openai
  api_base: http://localhost:8000/v1
  backend: gpt
```

### Cloud (OpenAI)
```yaml
vlm:
  model_name: gpt-4o
  api_key: ${OPENAI_API_KEY}
  api_base: https://api.openai.com/v1
  backend: gpt
```

---

## 3. Text Embedding (`Core/provider/embedding.py` — `TextEmbeddingProvider`)

**Config:** `graph.embedding_config` and `vdb.embedding_config` / `EmbeddingConfig`

Used for graph entity embeddings and text-only VDB indexing.

### Local (ModelScope)
```yaml
embedding_config:
  model_name: Qwen/Qwen3-Embedding-0.6B
  backend: local
  device: "cuda:2"
```

### Local (Ollama)
```yaml
embedding_config:
  model_name: nomic-embed-text
  backend: ollama
```

### Cloud (OpenAI)
```yaml
embedding_config:
  model_name: text-embedding-3-small
  backend: openai
  api_base: https://api.openai.com/v1
  api_key: ${OPENAI_API_KEY}
```

---

## 4. Text Reranker (`Core/provider/rerank.py` — `TextRerankerProvider`)

**Config:** `graph.reranker_config` and `rag.reranker_config` / `RerankerConfig`

Used in entity resolution (KG refinement) and RAG retrieval reranking.

### Local (ModelScope)
```yaml
reranker_config:
  model_name: Qwen/Qwen3-Reranker-4B
  backend: local
  device: "cuda:2"
```

### Local (vLLM)
```yaml
reranker_config:
  model_name: Qwen3-Reranker-4B
  backend: vllm
  api_base: "http://localhost:8011/v1"
```

### Cloud (Jina AI)
```yaml
reranker_config:
  model_name: jina-reranker-v2-base-multilingual
  backend: jina
  api_key: ${JINA_API_KEY}
```

---

## 5. Multimodal Embedding — GME (`Core/provider/embedding.py` — `GmeEmbeddingProvider`)

**Config:** `vdb.embedding_config` (when `vdb.mm_embedding: True`)

Local-only GPU singleton. Handles text, image, and fused (text+image) embeddings for the vector database and multimodal reranking.

**No cloud alternative** — the GME model runs multimodal fusion locally. To use cloud embeddings instead, set `vdb.mm_embedding: False` and configure `vdb.embedding_config` with an OpenAI-compatible text embedding backend. This loses image embedding but keeps text VDB functional.

```yaml
# Cloud fallback: text-only VDB
vdb:
  mm_embedding: False
  embedding_config:
    model_name: text-embedding-3-small
    backend: openai
    api_base: https://api.openai.com/v1
    api_key: ${OPENAI_API_KEY}
```

---

## 6. MinerU / PDF-Extract-Kit (`Core/provider/pdf_extracter.py`)

**Config:** `mineru` section / `MinerU`

Local PDF processing service. Requires a running SGLang VLM server.

```yaml
mineru:
  backend: vlm-sglang-client
  method: vlm
  server_url: http://localhost:30000
  lang: en
```

No cloud alternative currently. This service must run locally.

---

## Full Cloud Configuration

See `config/gbc-cloud.yaml` for a complete example that uses only cloud APIs (OpenAI + Jina), requiring no local GPU services.
