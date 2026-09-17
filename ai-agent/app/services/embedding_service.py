import os
import logging
from dotenv import load_dotenv
from openai import OpenAI

logger = logging.getLogger(__name__)
load_dotenv()

OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")

class EmbeddingService:
    def __init__(self):
        if not OPENAI_API_KEY:
            raise ValueError("OPENAI_API_KEY is missing from the environment variables.")

        self.client = OpenAI(api_key=OPENAI_API_KEY)
        self.model = "text-embedding-3-small"

    def generate_vector(self, text: str) -> list[float]:

        try:
            clean_text = text.replace("\n", " ").strip()

            response = self.client.embeddings.create(
                input=[clean_text],
                model=self.model
            )

            # Extract and return the float array
            return response.data[0].embedding

        except Exception as e:
            logger.error(f"Failed to generate embedding for text: '{text[:50]}...'. Error: {e}")
            raise