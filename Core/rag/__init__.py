from typing import Any, Union
from Core.configs.llm_config import LLMConfig
from Core.configs.vlm_config import VLMConfig
from Core.configs.rag_config import RAGConfig
from Core.rag.base_rag import BaseRAG

# Import the specific strategy config classes and the Union type
from Core.configs.rag import ALL_STRATEGY_CONFIGS
from Core.configs.rag.gbc_config import GBCRAGConfig

from Core.rag.gbc_rag import GBCRAG

from Core.provider.llm import LLM
from Core.provider.vlm import VLM

# Define the type for the strategy_config parameter
StrategyConfig = Union[*ALL_STRATEGY_CONFIGS]


def create_rag_agent(
    # The first parameter is now the specific strategy config object
    strategy_config: StrategyConfig,
    llm_config: LLMConfig,
    vlm_config: VLMConfig,
    **dependencies: Any,
) -> BaseRAG:
    """
    Factory function to create a RAG agent based on the provided strategy configuration.
    """
    # Get the strategy name directly from the config object for logging
    strategy_name = strategy_config.strategy
    print(f"INFO: Creating RAG agent with strategy: '{strategy_name}'")

    # 1. Initialize common dependencies
    llm_client = LLM(llm_config)
    vlm_client = VLM(vlm_config)

    # 2. Use isinstance for type-safe dispatching
    if isinstance(strategy_config, GBCRAGConfig):
        gbc_index = dependencies.get("gbc_index")
        return GBCRAG(
            llm=llm_client,
            vlm=vlm_client,
            config=strategy_config,
            gbc_index=gbc_index,
        )

    else:
        raise NotImplementedError(
            f"RAG agent for strategy '{strategy_name}' is not implemented."
        )
