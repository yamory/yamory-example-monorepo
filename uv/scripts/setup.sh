#!/bin/bash

# Setup script for the Python monorepo

set -e

echo "🚀 Setting up Python Monorepo..."

# Check if uv is installed
if ! command -v uv &>/dev/null; then
    echo "❌ uv is not installed. Please install uv first:"
    echo "curl -LsSf https://astral.sh/uv/install.sh | sh"
    exit 1
fi

# Install dependencies
echo "📦 Installing dependencies..."
uv sync

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo "📝 Creating .env file..."
    cat >.env <<EOF
# Database Configuration
DATABASE_URL=sqlite:///./app.db

# Redis Configuration
REDIS_URL=redis://localhost:6379/0

# API Configuration
API_HOST=0.0.0.0
API_PORT=8000
API_DEBUG=true

# Security
SECRET_KEY=your-secret-key-change-this-in-production

# Logging
LOG_LEVEL=INFO

# Environment
ENVIRONMENT=development
EOF
fi

# Initialize database
echo "🗄️ Initializing database..."
uv run --package cli python -m cli_app db-init

echo "✅ Setup completed successfully!"
echo ""
echo "Next steps:"
echo "1. Start the web API: uv run --package web-api uvicorn web_api.main:app --reload"
echo "2. Use the CLI: uv run --package cli python -m cli_app --help"
echo "3. Start the worker: uv run --package worker celery -A worker_app.celery_app worker --loglevel=info"
echo ""
echo "Happy coding! 🎉"
