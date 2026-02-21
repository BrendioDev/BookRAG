from typing import Dict, Any
from Core.configs.system_config import SystemConfig
import logging


log = logging.getLogger(__name__)


def prepare_rag_dependencies(cfg: SystemConfig) -> Dict[str, Any]:
    """
    根据配置加载并准备RAG agent所需的依赖项。
    这是一个调度函数，它知道哪种策略需要哪种资源。
    """

    rag_config = cfg.rag.strategy_config
    strategy_name = rag_config.strategy
    log.info(f"Preparing dependencies for RAG strategy: '{strategy_name}'")

    dependencies = {}

    if strategy_name == "gbc":
        from Core.Index.GBCIndex import GBC

        gbc_index = GBC.load_gbc_index(cfg)
        log.info(f"Successfully loaded GBC index from {cfg.save_path}")
        dependencies["gbc_index"] = gbc_index
    else:
        raise ValueError(f"Unknown or unsupported RAG strategy: '{strategy_name}'")

    return dependencies
