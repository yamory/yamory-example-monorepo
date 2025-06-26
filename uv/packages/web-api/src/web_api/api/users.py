"""
User management API endpoints.
"""

from typing import List

from fastapi import APIRouter, HTTPException, Depends, status
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError

from monorepo_core import db_manager, logger
from monorepo_core.models import User, UserCreate, UserResponse

router = APIRouter()


def get_db():
    """Database dependency."""
    return next(db_manager.get_session_dependency())


@router.get("/", response_model=List[UserResponse])
async def list_users(
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """List all users."""
    users = db.query(User).offset(skip).limit(limit).all()
    return users


@router.post("/", response_model=UserResponse, status_code=status.HTTP_201_CREATED)
async def create_user(
    user_data: UserCreate,
    db: Session = Depends(get_db)
):
    """Create a new user."""
    try:
        # Create new user
        db_user = User(
            email=user_data.email,
            username=user_data.username,
            full_name=user_data.full_name,
        )

        db.add(db_user)
        db.commit()
        db.refresh(db_user)

        logger.info(f"Created user: {db_user.username}")
        return db_user

    except IntegrityError as e:
        db.rollback()
        logger.error(f"Failed to create user: {e}")
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="User with this email or username already exists"
        )


@router.get("/{user_id}", response_model=UserResponse)
async def get_user(
    user_id: int,
    db: Session = Depends(get_db)
):
    """Get user by ID."""
    user = db.query(User).filter(User.id == user_id).first()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )

    return user


@router.put("/{user_id}", response_model=UserResponse)
async def update_user(
    user_id: int,
    user_data: UserCreate,
    db: Session = Depends(get_db)
):
    """Update user by ID."""
    user = db.query(User).filter(User.id == user_id).first()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )

    try:
        # Update user fields
        user.email = user_data.email
        user.username = user_data.username
        user.full_name = user_data.full_name

        db.commit()
        db.refresh(user)

        logger.info(f"Updated user: {user.username}")
        return user

    except IntegrityError as e:
        db.rollback()
        logger.error(f"Failed to update user: {e}")
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="User with this email or username already exists"
        )


@router.delete("/{user_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_user(
    user_id: int,
    db: Session = Depends(get_db)
):
    """Delete user by ID."""
    user = db.query(User).filter(User.id == user_id).first()

    if not user:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="User not found"
        )

    db.delete(user)
    db.commit()

    logger.info(f"Deleted user: {user.username}")
    return None