"""
Main CLI application using Typer.
"""

import typer
from rich.console import Console
from rich.table import Table
from typing import Optional

from monorepo_core import get_settings, db_manager, logger
from monorepo_core.models import User, UserCreate

app = typer.Typer(
    name="monorepo-cli",
    help="Command line interface for the Python monorepo utilities"
)
console = Console()


@app.command()
def info():
    """Show application information."""
    settings = get_settings()

    table = Table(title="Monorepo CLI Information")
    table.add_column("Setting", style="cyan")
    table.add_column("Value", style="green")

    table.add_row("Environment", settings.environment)
    table.add_row("Database URL", settings.database_url)
    table.add_row("Log Level", settings.log_level)
    table.add_row("API Host", settings.api_host)
    table.add_row("API Port", str(settings.api_port))

    console.print(table)


@app.command()
def db_init():
    """Initialize the database."""
    try:
        db_manager.create_tables()
        console.print("✅ Database initialized successfully!", style="green")
    except Exception as e:
        console.print(f"❌ Failed to initialize database: {e}", style="red")
        raise typer.Exit(1)


@app.command()
def user_list():
    """List all users."""
    try:
        with db_manager.get_session() as session:
            users = session.query(User).all()

            if not users:
                console.print("No users found.", style="yellow")
                return

            table = Table(title="Users")
            table.add_column("ID", style="cyan")
            table.add_column("Username", style="green")
            table.add_column("Email", style="blue")
            table.add_column("Full Name", style="magenta")
            table.add_column("Active", style="yellow")

            for user in users:
                table.add_row(
                    str(user.id),
                    user.username,
                    user.email,
                    user.full_name or "",
                    user.is_active
                )

            console.print(table)

    except Exception as e:
        console.print(f"❌ Failed to list users: {e}", style="red")
        raise typer.Exit(1)


@app.command()
def user_create(
    username: str = typer.Option(..., "--username", "-u", help="Username"),
    email: str = typer.Option(..., "--email", "-e", help="Email address"),
    full_name: Optional[str] = typer.Option(None, "--full-name", "-n", help="Full name"),
):
    """Create a new user."""
    try:
        with db_manager.get_session() as session:
            # Check if user already exists
            existing_user = session.query(User).filter(
                (User.username == username) | (User.email == email)
            ).first()

            if existing_user:
                console.print(f"❌ User with username '{username}' or email '{email}' already exists!", style="red")
                raise typer.Exit(1)

            # Create new user
            user = User(
                username=username,
                email=email,
                full_name=full_name,
            )

            session.add(user)
            session.commit()

            console.print(f"✅ User '{username}' created successfully!", style="green")

    except Exception as e:
        console.print(f"❌ Failed to create user: {e}", style="red")
        raise typer.Exit(1)


@app.command()
def user_delete(
    user_id: int = typer.Argument(..., help="User ID to delete")
):
    """Delete a user by ID."""
    try:
        with db_manager.get_session() as session:
            user = session.query(User).filter(User.id == user_id).first()

            if not user:
                console.print(f"❌ User with ID {user_id} not found!", style="red")
                raise typer.Exit(1)

            username = user.username
            session.delete(user)
            session.commit()

            console.print(f"✅ User '{username}' deleted successfully!", style="green")

    except Exception as e:
        console.print(f"❌ Failed to delete user: {e}", style="red")
        raise typer.Exit(1)


def main():
    """Main entry point."""
    app()


if __name__ == "__main__":
    main()