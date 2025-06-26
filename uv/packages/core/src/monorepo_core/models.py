"""
Base models and common database models.
"""

from datetime import datetime
from typing import Optional

from pydantic import BaseModel as PydanticBaseModel
from sqlalchemy import Column, Integer, DateTime, String
from sqlalchemy.sql import func

from .database import Base


class BaseModel(PydanticBaseModel):
    """Base Pydantic model with common configuration."""

    class Config:
        from_attributes = True
        validate_assignment = True
        arbitrary_types_allowed = True


class TimestampMixin:
    """Mixin for adding timestamp fields to SQLAlchemy models."""

    created_at = Column(
        DateTime(timezone=True),
        server_default=func.now(),
        nullable=False
    )
    updated_at = Column(
        DateTime(timezone=True),
        server_default=func.now(),
        onupdate=func.now(),
        nullable=False
    )


class BaseDBModel(Base, TimestampMixin):
    """Base SQLAlchemy model with common fields."""

    __abstract__ = True

    id = Column(Integer, primary_key=True, index=True)


# Example shared models
class User(BaseDBModel):
    """User model."""

    __tablename__ = "users"

    email = Column(String(255), unique=True, index=True, nullable=False)
    username = Column(String(100), unique=True, index=True, nullable=False)
    full_name = Column(String(255), nullable=True)
    is_active = Column(String(10), default="true", nullable=False)


# Pydantic schemas
class UserBase(BaseModel):
    """Base user schema."""
    email: str
    username: str
    full_name: Optional[str] = None


class UserCreate(UserBase):
    """User creation schema."""
    pass


class UserResponse(UserBase):
    """User response schema."""
    id: int
    is_active: str
    created_at: datetime
    updated_at: datetime