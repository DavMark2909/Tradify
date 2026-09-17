import logging

from app.consumers.product_consumer import ProductEventConsumer

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(message)s")
logger = logging.getLogger(__name__)

def main():
    logger.info("Starting Python AI Agent Microservice...")

    product_consumer = ProductEventConsumer()

    try:
        product_consumer.start_listening()
    except KeyboardInterrupt:
        logger.info("Service shutting down gracefully...")


if __name__ == "__main__":
    main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
