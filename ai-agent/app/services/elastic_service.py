import os
import logging
from dotenv import load_dotenv
from elasticsearch import Elasticsearch
from elasticsearch.exceptions import NotFoundError

# Load the .env file directly here to keep secrets out of config.py
load_dotenv()

logger = logging.getLogger(__name__)


class ElasticService:
    def __init__(self):
        # We connect to the single-node Docker container we spun up earlier
        self.es = Elasticsearch("http://localhost:9200")
        self.index_name = "b2b_products"
        self._create_index_if_not_exists()

    def _create_index_if_not_exists(self):
        """
        Creates the Elasticsearch index with the exact schema needed for
        Hybrid Search (Exact filters + Dense Vectors).
        """
        if self.es.indices.exists(index=self.index_name):
            return

        # The Mapping defines how each field is stored and searched
        mapping = {
            "mappings": {
                "properties": {
                    "productId": {"type": "long"},
                    "supplierCompanyId": {"type": "long"},
                    "sectorId": {"type": "long"},
                    "title": {"type": "text"},  # BM25 Full-text search
                    "description": {"type": "text"},  # BM25 Full-text search
                    "price": {"type": "double"},
                    "currency": {"type": "keyword"},  # Exact match only
                    "unitOfMeasure": {"type": "keyword"},
                    "availableQuantity": {"type": "double"},
                    "status": {"type": "keyword"},
                    "product_vector": {
                        "type": "dense_vector",
                        "dims": 1536,  # text-embedding-3-small dimensions
                        "index": True,
                        "similarity": "cosine"  # Industry standard for text embeddings
                    }
                }
            }
        }

        self.es.indices.create(index=self.index_name, body=mapping)
        logger.info(f"Successfully created Elasticsearch index: {self.index_name}")

    def index_product(self, payload_dict: dict, vector: list[float]):
        """
        Upserts the product and its vector into Elasticsearch.
        Uses the MySQL productId as the Elasticsearch document ID to prevent duplicates.
        """
        try:
            # Combine the raw SQL data with our newly generated AI vector
            doc = {**payload_dict, "product_vector": vector}

            self.es.index(
                index=self.index_name,
                id=str(payload_dict["productId"]),  # Keeps Elastic completely synced with MySQL
                document=doc
            )
            logger.info(f"Indexed Product ID: {payload_dict['productId']} to Elastic.")
        except Exception as e:
            logger.error(f"Failed to index product {payload_dict.get('productId')}: {e}")
            raise