# Python → Java Mapping

Reference for the BookRAG port from Python to Java.

## Entry Points

| Python | Java | Status |
|---|---|---|
| `main.py` | `com.bookrag.BookRagApplication` | Mock |
| `main.py:create_args()` | Picocli `@Command` + `@Option` annotations | Done |
| `main.py:build_index()` | `BookRagApplication.IndexCommand.run()` | Mock |
| `main.py:run_inference()` | `BookRagApplication.RagCommand.run()` | Mock |

## Config System

| Python | Java | Status |
|---|---|---|
| `Core/configs/system_config.py` → `SystemConfig` | `com.bookrag.core.config.SystemConfig` | Done |
| `Core/configs/system_config.py` → `load_system_config()` | `com.bookrag.core.config.ConfigLoader.loadSystemConfig()` | Done |
| `Core/configs/dataset_config.py` → `DatasetConfig` | `com.bookrag.core.config.DatasetConfig` | Done |
| `Core/configs/dataset_config.py` → `load_dataset_config()` | `com.bookrag.core.config.ConfigLoader.loadDatasetConfig()` | Done |
| `Core/configs/llm_config.py` → `LLMConfig` | `com.bookrag.core.config.LlmConfig` | Done |
| `Core/configs/vlm_config.py` → `VLMConfig` | `com.bookrag.core.config.VlmConfig` | Done |
| `Core/configs/mineru_config.py` → `MinerU` | `com.bookrag.core.config.MineruConfig` | Done |
| `Core/configs/tree_config.py` → `TreeConfig` | `com.bookrag.core.config.TreeConfig` | Done |
| `Core/configs/graph_config.py` → `GraphConfig` | `com.bookrag.core.config.GraphConfig` | Done |
| `Core/configs/vdb_config.py` → `VDBConfig` | `com.bookrag.core.config.VdbConfig` | Done |
| `Core/configs/embedding_config.py` → `EmbeddingConfig` | `com.bookrag.core.config.EmbeddingConfig` | Done |
| `Core/configs/rerank_config.py` → `RerankerConfig` | `com.bookrag.core.config.RerankerConfig` | Done |
| `Core/configs/rag_config.py` → `RAGConfig` | `com.bookrag.core.config.RagConfig` | Done |
| `Core/configs/rag/base_config.py` → `BaseRAGStrategyConfig` | `com.bookrag.core.config.rag.BaseRagStrategyConfig` | Done |
| `Core/configs/rag/gbc_config.py` → `GBCRAGConfig` | `com.bookrag.core.config.rag.GbcRagConfig` | Done |

### Config Notes

- Python uses Pydantic `BaseModel` / `@dataclass` → Java uses POJOs with Jackson `@JsonProperty`
- Python `snake_case` field names → Java `camelCase` with `@JsonProperty("snake_case")` for YAML compat
- Python `load_system_config()` wraps `rag` section inside `{"strategy_config": ...}` → Java `ConfigLoader` replicates this transform
- Python discriminated union (`discriminator="strategy"`) → Jackson `@JsonTypeInfo(property="strategy")` + `@JsonSubTypes`

## Index Data Structures

| Python | Java | Status |
|---|---|---|
| `Core/Index/Tree.py` → `NodeType` | `com.bookrag.core.index.NodeType` (enum) | Done |
| `Core/Index/Tree.py` → `MetaInfo` | `com.bookrag.core.index.MetaInfo` | Done |
| `Core/Index/Tree.py` → `TreeNode` | `com.bookrag.core.index.TreeNode` | Done |
| `Core/Index/Tree.py` → `DocumentTree` | `com.bookrag.core.index.DocumentTree` | Done |
| `Core/Index/Graph.py` → `Entity` | `com.bookrag.core.index.Entity` | Done |
| `Core/Index/Graph.py` → `Relationship` | `com.bookrag.core.index.Relationship` | Done |
| `Core/Index/Graph.py` → `Graph` | `com.bookrag.core.index.Graph` | Done |
| `Core/Index/GBCIndex.py` → `GBC` | `com.bookrag.core.index.GbcIndex` | Done |

### Data Structure Notes

- Python `pickle` serialization → Java serialization (TODO: implement)
- Python `networkx.Graph` → JGraphT `SimpleGraph` + separate `Map<String, Entity>` for node attributes
- Python `defaultdict(set)` for `tree2kg` → `HashMap<Integer, Set<String>>`
- `DocumentTree` methods like `getPathFromRoot`, `getSiblingNodes`, `getSubtreeNodes` are fully ported

## Common

| Python | Java | Status |
|---|---|---|
| `Core/Common/Message.py` → `Message` | `com.bookrag.core.common.Message` | Done |
| `Core/Common/Message.py` → `UserMessage`, `SystemMessage`, `AIMessage` | Not ported (subclasses, use `role` field) | TODO |
| `Core/Common/Memory.py` → `Memory` | `com.bookrag.core.common.Memory` | Done |

## Providers

| Python | Java | Status |
|---|---|---|
| `Core/provider/llm.py` → `BaseLLMController` / `LLM` | `com.bookrag.core.provider.LlmProvider` (interface) | Interface only |
| `Core/provider/vlm.py` → `BaseVLMController` / `VLM` | `com.bookrag.core.provider.VlmProvider` (interface) | Interface only |
| `Core/provider/embedding.py` → `BaseEmbedder` / `TextEmbeddingProvider` | `com.bookrag.core.provider.EmbeddingProvider` (interface) | Interface only |
| `Core/provider/rerank.py` → `TextRerankerProvider` | `com.bookrag.core.provider.RerankerProvider` (interface) | Interface only |
| `Core/provider/vdb.py` → `VectorStore` | `com.bookrag.core.provider.VectorStoreProvider` (interface) | Interface only |
| `Core/provider/TokenTracker.py` | `com.bookrag.core.provider.TokenTracker` | Done |
| Mock implementations | `com.bookrag.core.provider.mock.*` (5 classes) | Done |

### Provider Notes

- Python providers use OpenAI SDK client → Java should use `java.net.http.HttpClient` or OkHttp
- Python `GmeEmbeddingProvider` (multimodal) → not yet ported
- Python provider backends (local, openai, ollama, vllm) → implement as strategy pattern in Java

## Pipeline Stages

| Python | Java | Status |
|---|---|---|
| `Core/pipelines/pdf_refiner.py` | `com.bookrag.core.pipeline.PdfRefinerStage` | Mock |
| `Core/pipelines/outline_extractor.py` | `com.bookrag.core.pipeline.OutlineExtractorStage` | Mock |
| `Core/pipelines/doc_tree_builder.py` | `com.bookrag.core.pipeline.DocTreeBuilderStage` | Mock |
| `Core/pipelines/kg_extractor.py` | `com.bookrag.core.pipeline.KgExtractorStage` | Mock |
| `Core/pipelines/kg_builder.py` | `com.bookrag.core.pipeline.KgBuilderStage` | Mock |
| `Core/pipelines/kg_refiner.py` | `com.bookrag.core.pipeline.KgRefinerStage` | Mock |
| `Core/pipelines/vdb_index.py` | `com.bookrag.core.pipeline.VdbIndexStage` | Mock |
| `Core/pipelines/tree_node_builder.py` | Not yet ported | TODO |
| `Core/pipelines/tree_node_summary.py` | Not yet ported | TODO |
| `Core/construct_index.py` | `com.bookrag.core.pipeline.IndexConstructor` | Mock |

## RAG Layer

| Python | Java | Status |
|---|---|---|
| `Core/rag/base_rag.py` → `BaseRAG` | `com.bookrag.core.rag.BaseRag` | Done (abstract) |
| `Core/rag/gbc_rag.py` → `GBCRAG` | `com.bookrag.core.rag.GbcRag` | Mock |
| `Core/rag/__init__.py` → `create_rag_agent()` | `com.bookrag.core.rag.RagFactory` | Done |
| `Core/rag/gbc_retrieval.py` → `Retriever` | Not yet ported | TODO |
| `Core/rag/gbc_answer.py` → `AnswerAgent` | Not yet ported | TODO |
| `Core/rag/gbc_plan.py` → `TaskPlanner` | Not yet ported | TODO |
| `Core/rag/gbc_utils.py` | Not yet ported | TODO |
| `Core/inference.py` → `inference()`, `run_rag()` | Logic in `BookRagApplication.RagCommand.run()` | Mock |

## Not Yet Ported

| Python | Notes |
|---|---|
| `Core/utils/utils.py` | Token counting, JSON parsing/repair |
| `Core/utils/file_utils.py` | Indexing stats persistence |
| `Core/utils/table_utils.py` | HTML table parsing |
| `Core/utils/resource_loader.py` | RAG dependency loading |
| `Core/prompts/*` | All LLM prompt templates |
| `Eval/evaluation.py` | Evaluation orchestration |
| `Scripts/preprocess/*` | Dataset preprocessing notebooks |

## Library Mapping

| Python Library | Java Library | Purpose |
|---|---|---|
| `argparse` | Picocli | CLI argument parsing |
| `pydantic` | Jackson (POJOs + annotations) | Config validation / serialization |
| `yaml` (PyYAML) | `jackson-dataformat-yaml` | YAML parsing |
| `networkx` | JGraphT | Graph operations |
| `openai` (SDK) | `java.net.http.HttpClient` (TODO) | LLM API calls |
| `chromadb` | TBD (Chroma Java client or Milvus) | Vector database |
| `tiktoken` | TBD (jtokkit) | Token counting |
| `pandas` | TBD (not needed for core) | Data manipulation |
| `pickle` | Java Serialization or Jackson | Object persistence |
