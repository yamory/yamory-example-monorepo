"""Tests for API application."""

import pytest
from fastapi.testclient import TestClient
import sys
import os

# Add path for shared library
sys.path.append(os.path.join(os.path.dirname(__file__), '..', '..', 'shared'))

from main import app

client = TestClient(app)


def test_root():
    """Test root endpoint."""
    response = client.get("/")
    assert response.status_code == 200
    data = response.json()
    assert "message" in data
    assert "current_time" in data
    assert "version" in data


def test_health_check():
    """Test health check endpoint."""
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "healthy"
    assert "timestamp" in data


def test_create_user():
    """Test user creation."""
    user_data = {
        "name": "Test User",
        "email": "test@example.com"
    }
    response = client.post("/users", json=user_data)
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == user_data["name"]
    assert data["email"] == user_data["email"]
    assert data["is_active"] is True
    assert "id" in data
    assert "created_at" in data


def test_get_users():
    """Test getting all users."""
    # First create a user
    user_data = {
        "name": "Test User 2",
        "email": "test2@example.com"
    }
    client.post("/users", json=user_data)

    # Then get all users
    response = client.get("/users")
    assert response.status_code == 200
    data = response.json()
    assert isinstance(data, list)


def test_get_user_by_id():
    """Test getting user by ID."""
    # First create a user
    user_data = {
        "name": "Test User 3",
        "email": "test3@example.com"
    }
    create_response = client.post("/users", json=user_data)
    user_id = create_response.json()["id"]

    # Then get the user by ID
    response = client.get(f"/users/{user_id}")
    assert response.status_code == 200
    data = response.json()
    assert data["name"] == user_data["name"]
    assert data["email"] == user_data["email"]


def test_get_nonexistent_user():
    """Test getting nonexistent user."""
    response = client.get("/users/99999")
    assert response.status_code == 404