"""
Celery application configuration.
"""

from celery import Celery

from monorepo_core import get_settings, logger

# Get settings
settings = get_settings()

# Create Celery app
celery_app = Celery(
    "monorepo-worker",
    broker=settings.redis_url,
    backend=settings.redis_url,
    include=["worker_app.tasks"]
)

# Configure Celery
celery_app.conf.update(
    task_serializer="json",
    accept_content=["json"],
    result_serializer="json",
    timezone="UTC",
    enable_utc=True,
    result_expires=3600,
    task_track_started=True,
    task_time_limit=30 * 60,  # 30 minutes
    task_soft_time_limit=25 * 60,  # 25 minutes
    worker_prefetch_multiplier=1,
    worker_max_tasks_per_child=1000,
)

# Logging
logger.info("Celery app configured successfully")

if __name__ == "__main__":
    celery_app.start()