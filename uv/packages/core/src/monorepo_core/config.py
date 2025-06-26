"""
Configuration management using Pydantic settings.
"""

from functools import lru_cache
from typing import Optional

from pydantic import Field
from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    """Application settings."""

    # Database
    database_url: str = Field(
        default="sqlite:///./app.db",
        description="Database URL"
    )

    # Redis
    redis_url: str = Field(
        default="redis://localhost:6379/0",
        description="Redis URL for caching and message broker"
    )

    # API
    api_host: str = Field(default="0.0.0.0", description="API host")
    api_port: int = Field(default=8000, description="API port")
    api_debug: bool = Field(default=False, description="Debug mode")

    # Security
    secret_key: str = Field(
        default="your-secret-key-change-this-in-production",
        description="Secret key for JWT tokens"
    )

    # Logging
    log_level: str = Field(default="INFO", description="Log level")

    # Environment
    environment: str = Field(default="development", description="Environment name")

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"


@lru_cache()
def get_settings() -> Settings:
    """Get cached settings instance."""
    return Settings()