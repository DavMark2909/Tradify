import json
import logging
from confluent_kafka import Consumer, KafkaError, KafkaException
from pydantic import BaseModel
from typing import Optional
from decimal import Decimal

from app.config import KAFKA_BOOTSTRAP_SERVERS, PRODUCT_EVENTS_TOPIC, KAFKA_GROUP_ID
from app.services.embedding_service import EmbeddingService
from app.services.elastic_service import ElasticService

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
logger = logging.getLogger(__name__)


class ProductPayload(BaseModel):
    productId: int
    supplierCompanyId: int
    sectorId: int
    title: str
    description: Optional[str] = None
    price: Decimal
    currency: str
    unitOfMeasure: str
    availableQuantity: Decimal
    status: str


class ProductEvent(BaseModel):
    eventId: str
    eventType: str
    timestamp: str
    version: int
    payload: ProductPayload


# 2. The Consumer Class
class ProductEventConsumer:
    def __init__(self):
        self.conf = {
            'bootstrap.servers': KAFKA_BOOTSTRAP_SERVERS,
            'group.id': KAFKA_GROUP_ID,
            'auto.offset.reset': 'earliest',
            'enable.auto.commit': False,
        }
        self.consumer = Consumer(self.conf)
        self.embedding_service = EmbeddingService()
        self.elastic_service = ElasticService()

    def start_listening(self):
        self.consumer.subscribe([PRODUCT_EVENTS_TOPIC])
        logger.info(f"Subscribed to topic: {PRODUCT_EVENTS_TOPIC} under group: {KAFKA_GROUP_ID}")

        try:
            while True:
                msg = self.consumer.poll(timeout=1.0)
                if msg is None:
                    continue

                if msg.error():
                    if msg.error().code() == KafkaError._PARTITION_EOF:
                        continue
                    else:
                        raise KafkaException(msg.error())

                self._process_message(msg)

                # Commit the offset only AFTER successful processing
                self.consumer.commit(asynchronous=False)

        except KeyboardInterrupt:
            logger.info("Consumer stopped by user.")
        finally:
            self.consumer.close()
            logger.info("Kafka consumer connection closed.")

    def _process_message(self, msg):
        try:
            raw_value = msg.value().decode('utf-8')
            event_dict = json.loads(raw_value)

            event = ProductEvent(**event_dict)

            logger.info(f"Received event: {event.eventType} for Product ID: {event.payload.productId}")

            if event.eventType == "PRODUCT_CREATED":
                self._handle_product_created(event.payload)

        except Exception as e:
            logger.error(f"Failed to process message from partition {msg.partition()} at offset {msg.offset()}: {e}")

    def _handle_product_created(self, payload: ProductPayload):
        rich_text = f"{payload.title} {payload.description or ''} {payload.unitOfMeasure}".strip()

        # generation of the dense vector
        vector = self.embedding_service.generate_vector(rich_text)

        # saving to Elasticsearch for Hybrid Search
        self.elastic_service.index_product(payload.model_dump(), vector)

        logger.info(f"Successfully generated a {len(vector)}-dimensional vector for Product ID: {payload.productId}")