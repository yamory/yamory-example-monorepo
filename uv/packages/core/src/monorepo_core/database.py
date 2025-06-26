"""
Database management utilities.
"""

from contextlib import contextmanager
from typing import Generator

from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session

from .config import get_settings

# Create the declarative base
Base = declarative_base()


class DatabaseManager:
    """Database connection and session management."""

    def __init__(self, database_url: str = None):
        """Initialize database manager."""
        self.database_url = database_url or get_settings().database_url
        self.engine = create_engine(
            self.database_url,
            echo=get_settings().api_debug
        )
        self.SessionLocal = sessionmaker(
            autocommit=False,
            autoflush=False,
            bind=self.engine
        )

    def create_tables(self):
        """Create all tables."""
        Base.metadata.create_all(bind=self.engine)

    def drop_tables(self):
        """Drop all tables."""
        Base.metadata.drop_all(bind=self.engine)

    @contextmanager
    def get_session(self) -> Generator[Session, None, None]:
        """Get database session with automatic cleanup."""
        session = self.SessionLocal()
        try:
            yield session
            session.commit()
        except Exception:
            session.rollback()
            raise
        finally:
            session.close()

    def get_session_dependency(self) -> Generator[Session, None, None]:
        """Dependency for FastAPI to get database session."""
        session = self.SessionLocal()
        try:
            yield session
        finally:
            session.close()


# Global database manager instance
db_manager = DatabaseManager()