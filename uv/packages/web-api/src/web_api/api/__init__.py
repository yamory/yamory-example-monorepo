"""
API routes module.
"""

from fastapi import APIRouter

from .users import router as users_router

# Create main API router
api_router = APIRouter()

# Include sub-routers
api_router.include_router(users_router, prefix="/users", tags=["users"])