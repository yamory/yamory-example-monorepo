"""
Tests for configuration module.
"""

import pytest
from monorepo_core.config import Settings, get_settings


def test_default_settings():
    """Test default settings."""
    settings = Settings()

    assert settings.database_url == "sqlite:///./app.db"
    assert settings.api_host == "0.0.0.0"
    assert settings.api_port == 8000
    assert settings.environment == "development"
    assert settings.log_level == "INFO"


def test_get_settings():
    """Test get_settings function."""
    settings = get_settings()
    assert isinstance(settings, Settings)

    # Should return the same instance (cached)
    settings2 = get_settings()
    assert settings is settings2