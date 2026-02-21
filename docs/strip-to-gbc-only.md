# Strip Codebase to GBC-Only

Removed non-core RAG strategies (traverse, graph, vanilla/bm25/raptor, multi-modal) and the evaluation framework, keeping only the GBC RAG path.

## Motivation

GBC (Graph-Based Contextual) RAG is the paper's primary contribution — a hierarchical structure-aware retrieval approach that combines document tree traversal, knowledge graph reasoning, and vector similarity search into a unified pipeline. The other strategies existed as **experimental baselines** used during research to compare against GBC:

- **Vanilla/BM25/RAPTOR RAG**: Flat text retrieval baselines (embedding similarity, term matching, recursive abstractive summaries). They ignore document structure entirely and operate on raw text chunks, making them useful only as lower-bound comparisons.
- **Graph RAG**: A graph-only retrieval variant that uses the same GBC index but skips tree-based context and multi-source planning. It was an intermediate step toward GBC, not a standalone strategy.
- **Multi-Modal RAG (MMR)**: An image-centric retrieval path using GME multi-modal embeddings for reranking. It required a separate precomputation stage (`mm_reranker`) and its own VDB embedding pipeline. Orthogonal to GBC's structure-aware approach.
- **Traverse RAG**: A tree-walking agent that navigated the document outline using LLM-guided decisions. A proof-of-concept for tree-based retrieval that was superseded by GBC's integrated approach.

None of these alternatives are used in the final evaluation pipeline or production deployment. GBC subsumes the useful parts (tree structure, graph entities, vector search) while the baselines add dead code paths, unused config branches, and maintenance burden. Removing them simplifies the codebase to a single, well-tested path.

---

## Deleted Files

| Path | What it did | Who depended on it | Why safe to remove |
|------|-------------|--------------------|--------------------|
| `Eval/` (entire directory) | Orchestrated per-dataset evaluation (Qasper, M3Doc, MMLongBench) by running RAG inference and computing scores. Contained API keys, extraction prompts, and scoring utilities. | Standalone script (`python Eval/evaluation.py`), not imported by any `Core/` module. | Evaluation is a separate concern from the RAG system itself. No `Core/` code imports from `Eval/`. The `main.py rag` command already produces `final_results.json` output that can be scored externally. |
| `Core/rag/graph_rag.py` | `GraphRAG(BaseRAG)` — retrieved context using entity extraction and graph traversal only, without GBC's tree-based context or multi-source planning. Depended on `GBCIndex`, `GraphRAGConfig`. | Only instantiated via `create_rag_agent()` when `strategy == "graph"`. No other module imported it directly. | GBC already performs graph-based retrieval as one of its retrieval sources. Graph RAG was a subset of GBC's capabilities with no unique functionality. |
| `Core/rag/mm_rag.py` | `MMRAG(BaseRAG)` — multi-modal RAG that retrieved from a VDB built with GME multi-modal embeddings, then used VLM for answer generation. | Only instantiated via `create_rag_agent()` when `strategy == "mmr"`. Required a pre-built multi-modal VDB and embedding provider. | GBC handles multi-modal content (images, tables) through its own tree node processing and VLM integration. MMR's separate embedding pipeline (`compute_mm_embedding`) was an independent approach not used by GBC. |
| `Core/rag/vanilla_rag.py` | `VanillaRAG(BaseRAG)` — flat text retrieval using either vector similarity, BM25 term matching, or RAPTOR recursive summaries. No document structure awareness. | Only instantiated via `create_rag_agent()` when `strategy == "vanilla"`. Depended on `BM25` utility and `VectorStore`. | These are standard baseline methods with no structural understanding. GBC's vector search component already covers embedding-based retrieval, while BM25 and RAPTOR were research baselines only. |
| `Core/rag/traverse_agent.py` | `TraverseAgent(BaseRAG)` — LLM-guided tree walker that navigated the document outline node-by-node, deciding which branch to explore at each step. | Only instantiated via `create_rag_agent()` when `strategy == "traverse"`. Required a loaded `DocumentTree`. | GBC integrates tree-based retrieval directly (entity mapping to tree nodes, hierarchical context gathering) without the overhead of iterative LLM navigation calls. Traverse was a proof-of-concept. |
| `Core/configs/rag/graph_config.py` | `GraphRAGConfig(BaseRAGStrategyConfig)` — Pydantic config with `strategy: "graph"`, similarity thresholds, and topk parameters for graph-only retrieval. | Imported by `Core/configs/rag/__init__.py` (in `ALL_STRATEGY_CONFIGS`) and `Core/rag/__init__.py` (for factory dispatch). Used by `graph_rag.py`. | Config class for a deleted strategy. No remaining code references `GraphRAGConfig`. |
| `Core/configs/rag/mm_config.py` | `MMConfig(BaseRAGStrategyConfig)` — Pydantic config with `strategy: "mmr"`, VDB config, and topk parameter. | Imported by `Core/configs/rag/__init__.py` and `Core/rag/__init__.py`. Used by `mm_rag.py`. | Config class for a deleted strategy. No remaining code references `MMConfig`. |
| `Core/configs/rag/vanilla_config.py` | `VanillaConfig(BaseRAGStrategyConfig)` — Pydantic config with `strategy: "vanilla"`, VDB config, and `retrieval_method` field (vanilla/bm25/raptor/pdf_vanilla). | Imported by `Core/configs/rag/__init__.py` and `Core/rag/__init__.py`. Used by `vanilla_rag.py`. | Config class for a deleted strategy. No remaining code references `VanillaConfig`. |
| `Core/configs/rag/traverse_config.py` | `TraverseRAGConfig(BaseRAGStrategyConfig)` — Pydantic config with `strategy: "traverse"` and `max_depth` parameter. | Imported by `Core/configs/rag/__init__.py` and `Core/rag/__init__.py`. Used by `traverse_agent.py`. | Config class for a deleted strategy. No remaining code references `TraverseRAGConfig`. |
| `Core/prompts/traverseagent_prompt.py` | Prompt templates for the traverse agent: `NAVIGATOR_PROMPT_TEMPLATE` (LLM navigation decisions), `ANSWER_GENERATOR_INSTRUCTION_TEMPLATE`, and `NavigatorDecision` Pydantic schema. | Only imported by `Core/rag/traverse_agent.py`. | Prompts for a deleted strategy. No remaining code imports from this module. GBC uses its own prompts in `Core/prompts/gbc_prompt.py`. |
| `Core/utils/bm25.py` | `BM25` class — Okapi BM25 implementation with tokenization, index building, search, and pickle serialization. | Only imported by `Core/rag/vanilla_rag.py` (for BM25 retrieval) and `Core/pipelines/vdb_index.py` (for building BM25 indices). Also loaded by `resource_loader.py` in the vanilla branch. | BM25 is a term-matching baseline. GBC uses embedding-based vector search, not term frequency scoring. No GBC code path references BM25. |
| `Core/utils/raptor_utils.py` | `raptor_tree()` function — recursive abstractive processing that clusters text chunks via GMM, summarizes clusters with an LLM, and builds a multi-level summary tree. Used `TextEmbeddingProvider` and `LLM`. | Only imported by `Core/pipelines/vdb_index.py` in the `get_all_chunks()` function (raptor index type branch). | RAPTOR is a hierarchical summarization technique unrelated to GBC's document tree structure. GBC builds its tree from the actual document outline, not from clustering text chunks. No GBC code path references RAPTOR. |

---

## Edited Files

### `Core/configs/rag/__init__.py`

This file defines `ALL_STRATEGY_CONFIGS`, the tuple of all valid RAG strategy config classes. It's used by the factory pattern in `Core/rag/__init__.py` to build the `StrategyConfig` union type for type-safe dispatch. The removed imports (`TraverseRAGConfig`, `MMConfig`, `GraphRAGConfig`, `VanillaConfig`) registered strategies that no longer exist — keeping them would cause import errors since their source modules are deleted.

- Removed imports: `TraverseRAGConfig`, `MMConfig`, `GraphRAGConfig`, `VanillaConfig`
- `ALL_STRATEGY_CONFIGS` now contains only `GBCRAGConfig`

### `Core/rag/__init__.py`

This is the RAG agent factory — the central dispatch point that maps a strategy config to its implementation class. The original had `isinstance` branches for all five strategies (Traverse, GBC, Graph, Vanilla, MM), each importing its config and implementation. Since the non-GBC implementation modules are deleted, these imports and branches would fail at import time. The factory now has a single `GBCRAGConfig -> GBCRAG` path, which is the only one exercised in practice.

- Removed imports of all non-GBC strategy configs and implementations
- `create_rag_agent()` factory only dispatches to `GBCRAG` now

### `Core/utils/resource_loader.py`

This is the dependency injection dispatcher — it loads the correct index/store objects before the RAG agent is created. Each strategy needed different resources: Traverse needed a `DocumentTree`, GBC/Graph needed a `GBCIndex`, Vanilla needed a `VectorStore` or `BM25` index, and MMR needed a multi-modal `VectorStore`. The non-GBC branches loaded providers and indices that are no longer needed. The GBC branch (loading `GBCIndex`) is the only one that runs.

- `prepare_rag_dependencies()` only handles `strategy_name == "gbc"`
- Removed traverse, graph, vanilla, and mmr branches

### `Core/pipelines/vdb_index.py`

This module handles vector database construction during the offline indexing phase. The kept functions (`process_tree_nodes()` and `build_vdb_index()`) extract text/image content from the document tree and index them into a ChromaDB vector store — this is used by GBC's `construct_vdb()` pipeline stage. The removed functions served non-GBC index types: `get_input_text()`/`get_all_chunks()` read raw markdown for vanilla/BM25/RAPTOR chunking, `build_other_vdb_index()` built flat text VDBs or BM25 pickle indices, and `compute_mm_embedding()`/`compute_mm_embedding_question()` precomputed GME multi-modal embeddings for the MMR reranking pipeline.

- Removed functions: `get_input_text()`, `get_all_chunks()`, `build_other_vdb_index()`, `load_pdf_lists_from_dir()`, `compute_mm_embedding()`, `compute_mm_embedding_question()`
- Removed unused imports: `pandas`, `numpy`, `LLM`, `TextProcessor`, `raptor_tree`, `BM25`, `SystemConfig`, `Path`, `json`
- Kept: `process_tree_nodes()` and `build_vdb_index()` (used by GBC indexing)

### `Core/construct_index.py`

This is the high-level index construction orchestrator called by `main.py`. It coordinates tree building, knowledge graph extraction, and VDB indexing. The removed code handled two non-GBC concerns: (1) an early-return branch in `construct_vdb()` that diverted to `build_other_vdb_index()` for vanilla/BM25/RAPTOR index types, bypassing tree-based node processing entirely, and (2) the `compute_mm_reranker()` function that precomputed multi-modal embeddings for MMR's offline reranking stage. Neither code path is reachable when the index type is GBC.

- Removed imports: `build_other_vdb_index`, `compute_mm_embedding`, `compute_mm_embedding_question`, `pandas`
- Removed `compute_mm_reranker()` function
- Removed `if cfg.index_type in ["vanilla", "bm25", "raptor"]` branch in `construct_vdb()`

### `main.py`

The CLI entry point that wires together argparse, config loading, and the index/rag subcommands. The removed code served the MMR strategy: `compute_mm_reranker` was an import and pipeline stage for precomputing multi-modal embeddings (a separate offline step only MMR needed), and the `process_resource()` MMR branch configured a GPU device for MMR's embedding model. These were dead code paths since no config will select `strategy == "mmr"` anymore.

- Removed `compute_mm_reranker` import
- Removed `mm_reranker` from `--stage` argparse choices
- Removed `if stage == "mm_reranker"` block in `build_index()`
- Removed `if strategy == "mmr"` block in `process_resource()`

### `Core/inference.py`

This module runs RAG inference over a dataset, managing output directories and result persistence. The original `inference()` function had three branches for constructing `output_dir`: one for vanilla (using `retrieval_method` as suffix), one for GBC (using the `varient` field), and a generic fallback (using just `rag_strategy`). Since only GBC is supported, the output path is now always `eval_{dataset_name}_gbc_{varient}`, directly using the GBC config's `varient` field. The vanilla branch would have crashed anyway since `VanillaConfig` is deleted and its `retrieval_method` field doesn't exist on `GBCRAGConfig`.

- Simplified `inference()` output_dir logic to GBC-only path (uses `varient` from GBC config)
- Removed vanilla and generic fallback branches

---

## What's Preserved

- **GBC RAG code** (`Core/rag/gbc_rag.py`, `Core/rag/base_rag.py`) — The paper's core contribution. `gbc_rag.py` implements the full GBC pipeline: entity extraction from queries, entity-to-graph mapping, multi-source retrieval (graph neighbors + tree ancestors + vector similarity), retrieval planning, and augmented answer generation. `base_rag.py` defines the `BaseRAG` abstract class interface that GBC implements.

- **All pipeline stages** (`Core/pipelines/`) — Every offline indexing stage (PDF refinement, outline extraction, document tree building, KG extraction/building/refinement, VDB indexing) is used exclusively by GBC's index construction. None of these were shared with the removed strategies — vanilla/BM25/RAPTOR bypassed them entirely by working on raw text.

- **All providers** (`Core/provider/`) — LLM, VLM, embedding, rerank, and VDB provider abstractions are backend-agnostic wrappers used by GBC for inference (LLM/VLM), indexing (embedding), and retrieval (VDB, rerank). They are infrastructure, not strategy-specific.

- **All index structures** (`Core/Index/`) — `Tree.py`, `Graph.py`, and `GBCIndex.py` define the data structures that GBC builds during indexing and queries during retrieval. These are GBC's core data model.

- **YAML configs, docs, and Scripts/** — System configs (`config/*.yaml`) define LLM endpoints, tree/graph/VDB parameters, and the RAG strategy selection. Dataset configs (`Scripts/cfg/*.yaml`) are strategy-independent. These remain valid for GBC usage.

- **CLI entry point** (`main.py`) — The `index` subcommand with stages `tree`/`graph`/`vdb`/`all`/`rebuild_graph_vdb` and the `rag` subcommand are the standard way to run GBC. Only the `mm_reranker` stage was removed.
