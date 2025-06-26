"""
FastAPI application main module.
"""

from contextlib import asynccontextmanager
from typing import AsyncGenerator

from fastapi import FastAPI, HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session

from monorepo_core import get_settings, db_manager, logger
from monorepo_core.models import User, UserCreate, UserResponse

from .api import api_router


@asynccontextmanager
async def lifespan(app: FastAPI) -> AsyncGenerator[None, None]:
    """Application lifespan management."""
    # Startup
    logger.info("Starting up the web application...")
    db_manager.create_tables()

    yield

    # Shutdown
    logger.info("Shutting down the web application...")


def create_app() -> FastAPI:
    """Create and configure FastAPI application."""
    settings = get_settings()

    app = FastAPI(
        title="Monorepo Web API",
        description="A FastAPI application in a monorepo setup",
        version="0.1.0",
        debug=settings.api_debug,
        lifespan=lifespan,
    )

    # CORS middleware
    app.add_middleware(
        CORSMiddleware,
        allow_origins=["*"],  # Configure this properly for production
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    # Include API routes
    app.include_router(api_router, prefix="/api/v1")

    @app.get("/")
    async def root():
        """Root endpoint."""
        return {
            "message": "Welcome to Monorepo Web API",
            "version": "0.1.0",
            "environment": settings.environment,
        }

    @app.get("/health")
    async def health_check():
        """Health check endpoint."""
        return {"status": "healthy", "version": "0.1.0"}

    return app


# Create the application instance
app = create_app()