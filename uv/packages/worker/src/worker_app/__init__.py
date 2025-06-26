"""
Background worker using Celery.
"""

__version__ = "0.1.0"

from .celery_app import celery_app
from .tasks import process_user_task, send_email_task

__all__ = ["celery_app", "process_user_task", "send_email_task"]