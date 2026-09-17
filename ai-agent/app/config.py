import os
from dotenv import load_dotenv

# Loads variables from a local .env file into the environment
load_dotenv()

# Kafka Settings
KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092")
PRODUCT_EVENTS_TOPIC = os.getenv("PRODUCT_EVENTS_TOPIC", "product-events")
KAFKA_GROUP_ID = os.getenv("KAFKA_GROUP_ID", "ai-product-indexer-group")