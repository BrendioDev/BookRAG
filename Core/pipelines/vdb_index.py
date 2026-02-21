import os
from typing import Dict, List, Tuple

from Core.provider.embedding import (
    TextEmbeddingProvider,
    GmeEmbeddingProvider,
)
from Core.provider.vdb import VectorStore
from Core.Index.Tree import DocumentTree, NodeType
from Core.configs.vdb_config import VDBConfig
import logging

log = logging.getLogger(__name__)

save_path = "/home/wangshu/multimodal/GBC-RAG/test/sf/"


def process_tree_nodes(tree: DocumentTree) -> Tuple[Dict[str, List], Dict[str, List]]:
    text_list = []
    text_meta_data = []
    image_list = []
    image_meta_data = []
    image_str_list = []
    for node in tree.nodes:
        if node == tree.root_node:
            continue

        node_type = node.type
        meta_data = {
            "node_id": node.index_id,
            "pdf_id": node.meta_info.pdf_id,
        }

        if node_type == NodeType.IMAGE:
            image_path = node.meta_info.img_path
            image_str = node.meta_info.caption + node.meta_info.footnote
            text_list.append(image_str)
            text_meta_data.append(meta_data)

            # Check if the image path exists before adding it
            if image_path and os.path.exists(image_path):
                image_list.append(image_path)
                image_meta_data.append(meta_data)
                image_str_list.append(image_str)
        elif node_type == NodeType.TABLE:
            table_str = node.meta_info.content
            table_body = node.meta_info.table_body
            if table_body:
                table_str += table_body
            text_list.append(table_str)
            text_meta_data.append(meta_data)

            table_img = node.meta_info.img_path
            if table_img and os.path.exists(table_img):
                image_list.append(table_img)
                image_meta_data.append(meta_data)
                image_str_list.append(table_str)
        elif (
            node_type == NodeType.TEXT
            or node_type == NodeType.TITLE
            or node_type == NodeType.EQUATION
        ):
            text_content = node.meta_info.content
            if text_content:
                text_list.append(text_content)
                text_meta_data.append(meta_data)

    text_dict = {"text": text_list, "meta": text_meta_data}
    image_dict = {
        "image": image_list,
        "meta": image_meta_data,
        "image_str": image_str_list,
    }
    return text_dict, image_dict


def build_vdb_index(tree: DocumentTree, vdb_cfg: VDBConfig):
    if vdb_cfg.mm_embedding:
        embedder = GmeEmbeddingProvider(
            model_name=vdb_cfg.embedding_config.model_name,
            device=vdb_cfg.embedding_config.device,
        )
        log.info("Using GME multi-modal embedding model for vector database.")
    else:
        embedder = TextEmbeddingProvider(
            model_name=vdb_cfg.embedding_config.model_name,
            device=vdb_cfg.embedding_config.device,
            backend=vdb_cfg.embedding_config.backend,
            api_base=vdb_cfg.embedding_config.api_base,
            api_key=vdb_cfg.embedding_config.api_key,
            max_length=vdb_cfg.embedding_config.max_length,
        )
        log.info("Using text embedding model for vector database.")

    vdb = VectorStore(
        embedding_model=embedder,
        db_path=vdb_cfg.vdb_dir_name,
        collection_name=vdb_cfg.collection_name,
    )

    text_dict, image_dict = process_tree_nodes(tree)

    text, text_meta = text_dict["text"], text_dict["meta"]
    vdb.add_texts(texts=text, metadatas=text_meta)

    mm_vdb = vdb_cfg.mm_embedding
    if mm_vdb is True:
        image, img_meta, img_str = (
            image_dict["image"],
            image_dict["meta"],
            image_dict["image_str"],
        )
        vdb.add_images(image_paths=image, metadatas=img_meta, image_str=img_str)
        log.info("Images added to vector database successfully.")

    log.info("Vector database index built successfully.")

    vdb.embedding_model.close()  # Close the embedding model to free resources
    return


if __name__ == "__main__":
    print("test")
