# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

BookRAG is a hierarchical structure-aware index-based RAG system for complex documents (PDFs with text, images, tables, equations). Based on the VLDB 2026 paper. Uses MinerU/PDF-extract-kit-1.0 for PDF processing.

## Commands

### Index Construction (Offline)
```bash
python main.py -c config/gbc.yaml -d Scripts/cfg/example-Qasper.yaml index --stage all
```
Stages: `tree`, `graph`, `vdb`, `all`, `mm_reranker`, `rebuild_graph_vdb`

### RAG Inference (Online)
```bash
python main.py -c config/gbc.yaml -d Scripts/cfg/example-Qasper.yaml rag
```
Single query: add `-q "your question"`

### Evaluation
```bash
python Eval/evaluation.py -d Scripts/cfg/example-Qasper.yaml --method gbc
```

### Parallel Processing
Use `--nsplit N --num K` flags to split work across N processes (K = current split number).

## Architecture

Two-phase system: **offline index construction** then **online RAG inference**.

### Offline Pipeline
PDF → MinerU extraction → PDF refinement → outline extraction → document tree building → knowledge graph extraction → KG refinement → vector database indexing → unified GBC index (Tree + Graph + VDB)

### Online Pipeline (RAG strategies)
- **GBC RAG** (`gbc_rag.py`): Main approach — entity extraction → entity mapping → multi-source retrieval (graph + tree + text) → planning → answer generation
- **Graph RAG** (`graph_rag.py`): Knowledge graph focused
- **Vanilla RAG** (`vanilla_rag.py`): BM25 or embedding-based
- **Multi-Modal RAG** (`mm_rag.py`): Image/visual content emphasis
- **Traverse RAG** (`traverse_agent.py`): Tree-based traversal

### Key Data Structures
- **TreeNode**: Hierarchical document nodes (types: ROOT, TEXT, IMAGE, TABLE, EQUATION, TITLE) — `Core/Index/Tree.py`
- **Graph**: Entity-relationship knowledge graph — `Core/Index/Graph.py`
- **GBCIndex**: Unified index combining Tree + Graph + VDB — `Core/Index/GBCIndex.py`

## Code Organization

- `main.py` — CLI entry point (argparse with `index`/`rag` subcommands)
- `Core/configs/` — Pydantic config models; system, dataset, LLM, VLM, tree, graph, VDB, RAG configs
- `Core/pipelines/` — Index construction stages (pdf_refiner, outline_extractor, doc_tree_builder, kg_extractor, kg_builder, kg_refiner, vdb_index)
- `Core/provider/` — External service wrappers (LLM, VLM, embedding, rerank, VDB, PDF extraction)
- `Core/rag/` — RAG strategy implementations; `__init__.py` has `create_rag_agent` factory
- `Core/prompts/` — LLM prompt templates
- `Core/utils/` — Utilities (JSON parsing, token counting, BM25, file I/O)
- `Core/Common/` — Message abstraction and Memory storage
- `config/` — YAML system configs (gbc.yaml is the main one; variants: graph, vanilla, bm25, raptor, mm, tree)
- `Scripts/cfg/` — Dataset config examples
- `Eval/` — Evaluation orchestration and per-dataset eval utilities

## Configuration

Two config files required:
1. **System config** (`config/*.yaml`): LLM/VLM endpoints, tree/graph/VDB params, RAG strategy settings
2. **Dataset config** (`Scripts/cfg/*.yaml`): dataset path, working directory, dataset name

## Key Patterns

- **Pydantic models** for all configuration with YAML loading
- **Factory pattern**: `create_rag_agent()` in `Core/rag/__init__.py` returns strategy implementation based on config
- **Provider abstraction**: LLM/VLM/embedding/rerank providers support multiple backends (OpenAI, Ollama, vLLM)
- **Singleton**: `TokenTracker` in `Core/provider/TokenTracker.py` for global token usage tracking
- **BaseRAG abstract class**: All RAG strategies implement `_retrieve()`, `_create_augmented_prompt()`, `generation()`

## Dataset Format

Unified JSON array where each item has: `question`, `answer`, `doc_uuid`, `doc_path`, plus dataset-specific attributes.
