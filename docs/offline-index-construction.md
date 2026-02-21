# BookRAG Offline Index Construction — Workflow Documentation

## Context

This document captures the full offline index construction pipeline of BookRAG — how it transforms raw PDFs into a unified GBC (Graph + Tree + VDB) index for RAG inference.

---

## Execution Flow Overview

**Command:**
```bash
python main.py -c config/gbc.yaml -d Scripts/cfg/example-Qasper.yaml index --stage all
```

**Stages:** `tree` → `graph` → `vdb` (or `all` for everything, plus `mm_reranker`, `rebuild_graph_vdb`)

**Data Flow:**
```
Raw PDF
  ↓  [MinerU extraction]
middle_json + content_list
  ↓  [merge + PDF refinement]
Refined pdf_list
  ↓  [outline extraction via LLM]
title_outline (hierarchy)
  ↓  [tree construction]
DocumentTree (tree.pkl)
  ↓  [KG extraction via LLM or spaCy]
Raw entities + relationships
  ↓  [KG refinement — basic or advanced]
Graph (graph_data.json)
  ↓  [VDB indexing]
Tree_vdb/ (document node embeddings) + kg_vdb/ (entity embeddings)
  ↓
GBC Index (Tree + Graph + VDB) — ready for RAG
```

---

## Key Files by Pipeline Stage

### 1. Entry Point & Orchestration

| File | Role |
|------|------|
| `main.py` | CLI entry — parses args, loads configs, iterates documents, calls `build_index()` |
| `Core/construct_index.py` | Orchestrator — `construct_GBC_index()`, `construct_vdb()`, `rebuild_graph_vdb()` |

**`main.py:build_index()`** dispatches based on `--stage`:
- `tree` → `construct_GBC_index(config, tree_only=True)`
- `graph` → `construct_GBC_index(config)` (loads existing tree)
- `vdb` → `construct_vdb(config)` (loads existing tree)
- `all` → runs tree, then graph, then vdb sequentially

### 2. PDF Extraction & Refinement (Stage: tree)

| File | Role |
|------|------|
| `Core/provider/extract_pdf_info.py` | MinerU wrapper — `parse_doc()` extracts PDF into structured JSON |
| `Core/pipelines/pdf_refiner.py` | `pdf_info_refiner()` — LLM-based fixing of split paragraphs, formatting cleanup |
| `Core/pipelines/doc_tree_builder.py` | `build_tree_from_pdf()` — top-level function that chains extraction → refinement → outline → tree |

**Flow inside `build_tree_from_pdf()`:**
1. Call MinerU `parse_doc()` → get `middle_json` + `content_list`
2. `merge_middle_content()` → unified `pdf_list`
3. `pdf_info_refiner(pdf_list, llm)` → refined `pdf_list` (fixes broken paragraphs)
4. Proceed to outline extraction and tree construction

**Caching:** Checks for `{method}/{filename}_merged_content.json` to skip re-extraction.

### 3. Outline Extraction (Stage: tree)

| File | Role |
|------|------|
| `Core/pipelines/outline_extractor.py` | `extract_pdf_outline_in_chunks()` — LLM-validated hierarchical outline |
| `Core/prompts/` | Prompt templates including `OUTLINE_EXTRACTION_PROMPT` |

**Process:**
1. Filter text items that have `text_level` (from MinerU's layout detection)
2. Stack-based algorithm to infer parent-child relationships
3. LLM validates/corrects the hierarchy levels
4. `outline_refine()` ensures proper nesting

**Output:** `title_outline` — list of `{pdf_id, text_level, parent_id, end_id, ...}`

### 4. Document Tree Construction (Stage: tree)

| File | Role |
|------|------|
| `Core/pipelines/doc_tree_builder.py` | `construct_tree_index()` — builds tree from pdf_list + outline |
| `Core/pipelines/tree_node_builder.py` | `create_node_by_type()` — creates typed TreeNode from content items |
| `Core/pipelines/tree_node_summary.py` | `generate_tree_node_summary()` — optional LLM/VLM summaries per node |
| `Core/Index/Tree.py` | `DocumentTree`, `TreeNode`, `NodeType`, `MetaInfo` data structures |

**Node Types:** ROOT, TITLE, TEXT, IMAGE, TABLE, EQUATION, UNKNOWN

**TreeNode key fields:** `children`, `parent`, `type`, `meta_info` (content, page_idx, img_path, caption), `depth`, `index_id`, `summary`

**DocumentTree key fields:** `nodes` (flat list), `root_node`, `pdf_id_to_index_id` mapping

**Output:** `tree.pkl` (serialized) + `tree.json` (visualization)

### 5. Knowledge Graph Extraction (Stage: graph)

| File | Role |
|------|------|
| `Core/pipelines/kg_extractor.py` | Entity/relationship extraction — LLM or spaCy based |
| `Core/pipelines/kg_builder.py` | `build_knowledge_graph()` — orchestrates extraction for all nodes |
| `Core/prompts/kg_prompt.py` | Prompts: `ENTITY_EXTRACTION`, `TABLE_ENTITY_EXTRACTION`, `IMAGE_ENTITY_EXTRACTION`, `SECTION_ENTITY_EXTRACTION` |

**Extractor types** (`cfg.graph.extractor_type`):
- `"llm"`: OpenAI API with structured prompts + Pydantic models
- `"local"`: spaCy transformer models (NER + dependency parsing)

**Two extraction batches:**
1. **Title nodes** via `batch_extract_titles()` — with context (hierarchy path, siblings)
2. **Non-title nodes** via `batch_extract_kg()` — standard extraction

**Output:** List of `{node_idx, entities, relations}` per tree node

### 6. Knowledge Graph Refinement (Stage: graph)

| File | Role |
|------|------|
| `Core/pipelines/kg_refiner.py` | `KGRefiner` — deduplicates and merges entities |
| `Core/Index/Graph.py` | `Graph`, `Entity`, `Relationship` data structures |

**Refinement strategies** (`cfg.graph.refine_type`):
- **`"basic"`**: Merge entities with identical `(name, type)` pairs, aggregate `source_ids`
- **`"advanced"`**: Embedding similarity → reranker ranking → LLM-based entity merging with alias tracking

**Graph key fields:**
- `kg`: NetworkX undirected graph (entity nodes, relationship edges)
- `tree2kg`: Dict mapping tree_node_id → set of entity names (bidirectional link)

**Output:** `graph_data.json` or `graph_data_basic.json`

### 7. Vector Database Indexing (Stage: vdb)

| File | Role |
|------|------|
| `Core/pipelines/vdb_index.py` | `build_vdb_index()` — embeds tree nodes into ChromaDB |
| `Core/provider/vdb.py` | `VectorStore` — ChromaDB wrapper |
| `Core/provider/embedding.py` | `TextEmbeddingProvider`, `GmeEmbeddingProvider` (multi-modal) |

**Two VDBs are built:**
1. **Tree VDB** (`Tree_vdb/`): Document node content embeddings — built by `build_vdb_index()`
2. **Entity VDB** (`kg_vdb/`): KG entity embeddings — built by `GBCIndex.rebuild_vdb()`

**Embedding options:**
- `mm_embedding=True`: `GmeEmbeddingProvider` (Alibaba GME Qwen2-VL, text+image)
- `mm_embedding=False`: `TextEmbeddingProvider` (text-only)

### 8. GBC Index Assembly

| File | Role |
|------|------|
| `Core/Index/GBCIndex.py` | `GBC` class — unifies Tree + Graph + entity VDB |

**Key methods:**
- `save_gbc_index()` — persists tree.pkl + graph_data.json
- `rebuild_vdb()` — rebuilds entity VDB from graph
- `load_gbc_index(config)` — class method to reload from disk

---

## Configuration Files

| File | Role |
|------|------|
| `Core/configs/system_config.py` | `SystemConfig` — top-level Pydantic model |
| `Core/configs/tree_config.py` | `TreeConfig` — node_summary, use_vlm |
| `Core/configs/graph_config.py` | `GraphConfig` — extractor_type, refine_type, embedding, reranker |
| `Core/configs/vdb_config.py` | `VDBConfig` — mm_embedding, vdb_dir_name, embedding_config |
| `config/gbc.yaml` | Main system config (LLM endpoints, model names, all params) |
| `Scripts/cfg/example-Qasper.yaml` | Dataset config (paths, dataset name) |

---

## External Service Providers

| File | Role |
|------|------|
| `Core/provider/llm.py` | LLM calls — OpenAI API, Ollama backends |
| `Core/provider/vlm.py` | Vision-language model for image understanding |
| `Core/provider/embedding.py` | Text and multi-modal embeddings |
| `Core/provider/rerank.py` | Reranker for entity resolution |
| `Core/provider/vdb.py` | ChromaDB vector store wrapper |
| `Core/provider/extract_pdf_info.py` | MinerU PDF extraction |
| `Core/provider/TokenTracker.py` | Singleton token usage tracking |

---

## Output Directory Structure

```
save_path/{doc_uuid}/
├── tree.pkl                    # Serialized DocumentTree
├── tree.json                   # JSON visualization of tree
├── graph_data.json             # Graph entities + relationships
├── Tree_vdb/                   # ChromaDB — document node embeddings
├── kg_vdb/ (or kg_vdb_basic/)  # ChromaDB — entity embeddings
├── run_config.yaml             # Config snapshot
├── indexing_stats.json         # Timing + token usage per stage
└── logs/
    └── run_*.log               # Execution logs
```
