"""
Core utilities and shared code for the monorepo.
"""

__version__ = "0.1.0"

from .config import Settings, get_settings
from .database import Base, DatabaseManager, db_manager
from .models import BaseModel
from .utils import logger

__all__ = [
    "Settings",
    "get_settings",
    "Base",
    "DatabaseManager",
    "db_manager",
    "BaseModel",
    "logger",
]