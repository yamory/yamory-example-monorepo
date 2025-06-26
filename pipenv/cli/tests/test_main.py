"""Tests for CLI application."""

import pytest
from click.testing import CliRunner
import sys
import os

# Add path for shared library
sys.path.append(os.path.join(os.path.dirname(__file__), '..', '..', 'shared'))

from main import cli


def test_cli_version():
    """Test CLI version command."""
    runner = CliRunner()
    result = runner.invoke(cli, ['--version'])
    assert result.exit_code == 0
    assert "1.0.0" in result.output


def test_time_command():
    """Test time command."""
    runner = CliRunner()
    result = runner.invoke(cli, ['time'])
    assert result.exit_code == 0
    assert "Current Time" in result.output


def test_user_create_command():
    """Test user create command."""
    runner = CliRunner()
    result = runner.invoke(cli, ['user', 'create'], input='Test User\ntest@example.com\n')
    assert result.exit_code == 0
    assert "User created successfully" in result.output


def test_user_show_command():
    """Test user show command."""
    runner = CliRunner()
    result = runner.invoke(cli, ['user', 'show', '1'])
    assert result.exit_code == 0
    assert "User #1" in result.output


def test_cli_help():
    """Test CLI help."""
    runner = CliRunner()
    result = runner.invoke(cli, ['--help'])
    assert result.exit_code == 0
    assert "Multi-Project CLI Tool" in result.output