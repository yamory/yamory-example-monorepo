"""Command line interface tool."""

import sys
import os
import click
import json
from rich.console import Console
from rich.table import Table
from rich.panel import Panel

# Add shared library to path
sys.path.append(os.path.join(os.path.dirname(__file__), '..', 'shared'))

from shared.utils import DateHelper, HTTPClient
from shared.models import User

console = Console()


@click.group()
@click.version_option(version="1.0.0")
def cli():
    """Multi-Project CLI Tool

    A command line interface that uses the shared library and can interact with the API.
    """
    pass


@cli.group()
def user():
    """User management commands."""
    pass


@cli.group()
def api():
    """API interaction commands."""
    pass


@cli.command()
def time():
    """Show current time using shared library."""
    current_time = DateHelper.now()
    formatted_time = DateHelper.format_date(current_time)

    panel = Panel(
        f"[bold blue]{formatted_time}[/bold blue]",
        title="Current Time",
        border_style="blue"
    )
    console.print(panel)


@user.command()
@click.option('--name', prompt='User name', help='The user name')
@click.option('--email', prompt='User email', help='The user email')
def create(name: str, email: str):
    """Create a new user locally."""
    try:
        user_data = User(
            id=1,  # This would normally be generated
            name=name,
            email=email,
            created_at=DateHelper.now()
        )

        console.print("\n[green]✓[/green] User created successfully!")

        table = Table(title="User Details")
        table.add_column("Field", style="cyan")
        table.add_column("Value", style="magenta")

        table.add_row("ID", str(user_data.id))
        table.add_row("Name", user_data.name)
        table.add_row("Email", user_data.email)
        table.add_row("Active", str(user_data.is_active))
        table.add_row("Created", DateHelper.format_date(user_data.created_at))

        console.print(table)
    except Exception as e:
        console.print(f"[red]✗[/red] Error creating user: {e}")


@user.command()
@click.argument('user_id', type=int)
def show(user_id: int):
    """Show user details (mock implementation)."""
    # This would normally fetch from a database
    mock_user = User(
        id=user_id,
        name=f"User {user_id}",
        email=f"user{user_id}@example.com",
        created_at=DateHelper.add_days(DateHelper.now(), -10)
    )

    table = Table(title=f"User #{user_id}")
    table.add_column("Field", style="cyan")
    table.add_column("Value", style="magenta")

    table.add_row("ID", str(mock_user.id))
    table.add_row("Name", mock_user.name)
    table.add_row("Email", mock_user.email)
    table.add_row("Active", str(mock_user.is_active))
    table.add_row("Created", DateHelper.format_date(mock_user.created_at))

    console.print(table)


@api.command()
@click.option('--url', default='http://localhost:8000', help='API base URL')
def status(url: str):
    """Check API status."""
    try:
        client = HTTPClient(url)
        response = client.get('/health')

        if response.status_code == 200:
            data = response.json()
            console.print("[green]✓[/green] API is healthy!")
            console.print(f"Timestamp: {data.get('timestamp', 'N/A')}")
        else:
            console.print(f"[red]✗[/red] API returned status code: {response.status_code}")
    except Exception as e:
        console.print(f"[red]✗[/red] Error connecting to API: {e}")


@api.command()
@click.option('--url', default='http://localhost:8000', help='API base URL')
def users(url: str):
    """List users from API."""
    try:
        client = HTTPClient(url)
        response = client.get('/users')

        if response.status_code == 200:
            users_data = response.json()

            if not users_data:
                console.print("[yellow]No users found[/yellow]")
                return

            table = Table(title="API Users")
            table.add_column("ID", style="cyan")
            table.add_column("Name", style="magenta")
            table.add_column("Email", style="blue")
            table.add_column("Active", style="green")
            table.add_column("Created", style="yellow")

            for user in users_data:
                table.add_row(
                    str(user['id']),
                    user['name'],
                    user['email'],
                    str(user['is_active']),
                    user['created_at']
                )

            console.print(table)
        else:
            console.print(f"[red]✗[/red] API returned status code: {response.status_code}")
    except Exception as e:
        console.print(f"[red]✗[/red] Error fetching users: {e}")


@api.command()
@click.option('--url', default='http://localhost:8000', help='API base URL')
@click.option('--name', prompt='User name', help='The user name')
@click.option('--email', prompt='User email', help='The user email')
def create_user(url: str, name: str, email: str):
    """Create a user via API."""
    try:
        client = HTTPClient(url)
        user_data = {
            "name": name,
            "email": email
        }
        response = client.post('/users', user_data)

        if response.status_code == 200:
            user = response.json()
            console.print("[green]✓[/green] User created via API!")

            table = Table(title="Created User")
            table.add_column("Field", style="cyan")
            table.add_column("Value", style="magenta")

            table.add_row("ID", str(user['id']))
            table.add_row("Name", user['name'])
            table.add_row("Email", user['email'])
            table.add_row("Active", str(user['is_active']))
            table.add_row("Created", user['created_at'])

            console.print(table)
        else:
            console.print(f"[red]✗[/red] API returned status code: {response.status_code}")
    except Exception as e:
        console.print(f"[red]✗[/red] Error creating user: {e}")


if __name__ == '__main__':
    cli()