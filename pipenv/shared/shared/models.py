"""Common data models used across projects."""

from typing import Optional
from datetime import datetime
from pydantic import BaseModel as PydanticBaseModel, EmailStr


class BaseModel(PydanticBaseModel):
    """Base model with common functionality."""

    created_at: datetime = datetime.now()
    updated_at: Optional[datetime] = None

    class Config:
        json_encoders = {
            datetime: lambda dt: dt.isoformat()
        }


class User(BaseModel):
    """User model."""

    id: int
    name: str
    email: str
    is_active: bool = True

    def to_dict(self) -> dict:
        """Convert to dictionary."""
        return self.dict()

    @classmethod
    def from_dict(cls, data: dict) -> "User":
        """Create from dictionary."""
        return cls(**data)