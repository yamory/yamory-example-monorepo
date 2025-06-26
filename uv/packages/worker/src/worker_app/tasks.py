"""
Celery tasks for background processing.
"""

import time
from typing import Dict, Any
from celery import current_task

from monorepo_core import logger, db_manager
from monorepo_core.models import User

from .celery_app import celery_app


@celery_app.task(bind=True)
def process_user_task(self, user_id: int, action: str = "welcome") -> Dict[str, Any]:
    """
    Process user-related tasks.

    Args:
        user_id: User ID to process
        action: Action to perform (welcome, notification, etc.)

    Returns:
        Dictionary with task result
    """
    try:
        # Update task state
        current_task.update_state(
            state="PROGRESS",
            meta={"step": "Starting user processing", "progress": 0}
        )

        # Get user from database
        with db_manager.get_session() as session:
            user = session.query(User).filter(User.id == user_id).first()

            if not user:
                raise ValueError(f"User with ID {user_id} not found")

            logger.info(f"Processing user: {user.username} (action: {action})")

            # Update progress
            current_task.update_state(
                state="PROGRESS",
                meta={"step": "Processing user data", "progress": 25}
            )

            # Simulate some processing time
            time.sleep(2)

            # Update progress
            current_task.update_state(
                state="PROGRESS",
                meta={"step": "Performing action", "progress": 50}
            )

            # Perform the action
            if action == "welcome":
                result = _send_welcome_email(user)
            elif action == "notification":
                result = _send_notification(user)
            else:
                result = {"message": f"Unknown action: {action}"}

            # Update progress
            current_task.update_state(
                state="PROGRESS",
                meta={"step": "Finalizing", "progress": 75}
            )

            time.sleep(1)

            # Final result
            final_result = {
                "user_id": user_id,
                "username": user.username,
                "action": action,
                "result": result,
                "status": "completed"
            }

            logger.info(f"Completed processing user {user.username}")
            return final_result

    except Exception as e:
        logger.error(f"Error processing user {user_id}: {str(e)}")
        current_task.update_state(
            state="FAILURE",
            meta={"error": str(e), "user_id": user_id}
        )
        raise


@celery_app.task(bind=True)
def send_email_task(
    self,
    to_email: str,
    subject: str,
    body: str,
    from_email: str = None
) -> Dict[str, Any]:
    """
    Send email task.

    Args:
        to_email: Recipient email address
        subject: Email subject
        body: Email body
        from_email: Sender email address

    Returns:
        Dictionary with task result
    """
    try:
        # Update task state
        current_task.update_state(
            state="PROGRESS",
            meta={"step": "Preparing email", "progress": 0}
        )

        logger.info(f"Sending email to {to_email}")

        # Update progress
        current_task.update_state(
            state="PROGRESS",
            meta={"step": "Connecting to email server", "progress": 25}
        )

        # Simulate email sending
        time.sleep(2)

        # Update progress
        current_task.update_state(
            state="PROGRESS",
            meta={"step": "Sending email", "progress": 75}
        )

        time.sleep(1)

        # In a real application, you would use a proper email service
        # For now, we'll just log the email
        logger.info(f"Email sent to {to_email}: {subject}")

        result = {
            "to_email": to_email,
            "subject": subject,
            "status": "sent",
            "message": "Email sent successfully"
        }

        return result

    except Exception as e:
        logger.error(f"Error sending email to {to_email}: {str(e)}")
        current_task.update_state(
            state="FAILURE",
            meta={"error": str(e), "to_email": to_email}
        )
        raise


def _send_welcome_email(user: User) -> Dict[str, Any]:
    """Send welcome email to user."""
    subject = f"Welcome to our platform, {user.username}!"
    body = f"""
    Hello {user.full_name or user.username},

    Welcome to our platform! We're excited to have you on board.

    Your account details:
    - Username: {user.username}
    - Email: {user.email}

    Best regards,
    The Team
    """

    # Queue email sending task
    send_email_task.delay(
        to_email=user.email,
        subject=subject,
        body=body
    )

    return {
        "action": "welcome_email_queued",
        "email": user.email,
        "message": "Welcome email queued for sending"
    }


def _send_notification(user: User) -> Dict[str, Any]:
    """Send notification to user."""
    subject = f"Notification for {user.username}"
    body = f"""
    Hello {user.full_name or user.username},

    This is a notification message for your account.

    Best regards,
    The Team
    """

    # Queue email sending task
    send_email_task.delay(
        to_email=user.email,
        subject=subject,
        body=body
    )

    return {
        "action": "notification_queued",
        "email": user.email,
        "message": "Notification queued for sending"
    }