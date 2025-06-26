"""FastAPI web application."""

import sys
import os
from typing import List
from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel

# Add shared library to path
sys.path.append(os.path.join(os.path.dirname(__file__), '..', 'shared'))

from shared.models import User
from shared.utils import DateHelper

app = FastAPI(
    title="Multi-Project API",
    description="A sample API using shared library",
    version="1.0.0"
)

# In-memory storage for demo
users_db: List[User] = []
next_user_id = 1


class CreateUserRequest(BaseModel):
    """Request model for creating users."""
    name: str
    email: str


class UserResponse(BaseModel):
    """Response model for user data."""
    id: int
    name: str
    email: str
    is_active: bool
    created_at: str


@app.get("/")
async def root():
    """Root endpoint."""
    return {
        "message": "Welcome to Multi-Project API",
        "current_time": DateHelper.format_date(DateHelper.now()),
        "version": "1.0.0"
    }


@app.get("/health")
async def health_check():
    """Health check endpoint."""
    return {
        "status": "healthy",
        "timestamp": DateHelper.format_date(DateHelper.now())
    }


@app.get("/users", response_model=List[UserResponse])
async def get_users():
    """Get all users."""
    return [
        UserResponse(
            id=user.id,
            name=user.name,
            email=user.email,
            is_active=user.is_active,
            created_at=DateHelper.format_date(user.created_at)
        )
        for user in users_db
    ]


@app.get("/users/{user_id}", response_model=UserResponse)
async def get_user(user_id: int):
    """Get user by ID."""
    user = next((u for u in users_db if u.id == user_id), None)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    return UserResponse(
        id=user.id,
        name=user.name,
        email=user.email,
        is_active=user.is_active,
        created_at=DateHelper.format_date(user.created_at)
    )


@app.post("/users", response_model=UserResponse)
async def create_user(user_request: CreateUserRequest):
    """Create a new user."""
    global next_user_id

    user = User(
        id=next_user_id,
        name=user_request.name,
        email=user_request.email,
        created_at=DateHelper.now()
    )

    users_db.append(user)
    next_user_id += 1

    return UserResponse(
        id=user.id,
        name=user.name,
        email=user.email,
        is_active=user.is_active,
        created_at=DateHelper.format_date(user.created_at)
    )


@app.delete("/users/{user_id}")
async def delete_user(user_id: int):
    """Delete user by ID."""
    global users_db

    user_index = next((i for i, u in enumerate(users_db) if u.id == user_id), None)
    if user_index is None:
        raise HTTPException(status_code=404, detail="User not found")

    deleted_user = users_db.pop(user_index)
    return {
        "message": f"User {deleted_user.name} deleted successfully",
        "timestamp": DateHelper.format_date(DateHelper.now())
    }


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)