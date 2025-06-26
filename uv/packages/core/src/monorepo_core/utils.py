"""
Common utility functions and helpers.
"""

import logging
import sys
from typing import Any, Dict, Optional

from .config import get_settings


def setup_logger(
    name: str = "monorepo",
    level: Optional[str] = None,
    format_string: Optional[str] = None
) -> logging.Logger:
    """Setup and configure logger."""
    settings = get_settings()
    log_level = level or settings.log_level

    # Create logger
    logger = logging.getLogger(name)
    logger.setLevel(getattr(logging, log_level.upper()))

    # Remove existing handlers
    for handler in logger.handlers[:]:
        logger.removeHandler(handler)

    # Create console handler
    handler = logging.StreamHandler(sys.stdout)
    handler.setLevel(getattr(logging, log_level.upper()))

    # Create formatter
    if not format_string:
        format_string = (
            "%(asctime)s - %(name)s - %(levelname)s - "
            "%(filename)s:%(lineno)d - %(message)s"
        )

    formatter = logging.Formatter(format_string)
    handler.setFormatter(formatter)

    # Add handler to logger
    logger.addHandler(handler)

    return logger


# Global logger instance
logger = setup_logger()


def format_error_response(
    error: Exception,
    status_code: int = 500,
    detail: Optional[str] = None
) -> Dict[str, Any]:
    """Format error response for APIs."""
    return {
        "error": True,
        "status_code": status_code,
        "error_type": type(error).__name__,
        "message": str(error),
        "detail": detail,
    }


def validate_email(email: str) -> bool:
    """Simple email validation."""
    import re
    pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
    return re.match(pattern, email) is not None


def generate_slug(text: str, max_length: int = 50) -> str:
    """Generate URL-friendly slug from text."""
    import re

    # Convert to lowercase and replace spaces with hyphens
    slug = re.sub(r'[^\w\s-]', '', text.lower())
    slug = re.sub(r'[-\s]+', '-', slug)

    # Trim to max length
    if len(slug) > max_length:
        slug = slug[:max_length].rstrip('-')

    return slug