"""Tests for utility functions."""

import pytest
from datetime import datetime
from shared.utils import DateHelper, HTTPClient


class TestDateHelper:
    """Test DateHelper class."""

    def test_now(self):
        """Test now method."""
        now = DateHelper.now()
        assert isinstance(now, datetime)

    def test_parse_date(self):
        """Test parse_date method."""
        date_str = "2023-12-01 10:30:00"
        parsed = DateHelper.parse_date(date_str)
        assert parsed.year == 2023
        assert parsed.month == 12
        assert parsed.day == 1

    def test_format_date(self):
        """Test format_date method."""
        dt = datetime(2023, 12, 1, 10, 30, 0)
        formatted = DateHelper.format_date(dt)
        assert formatted == "2023-12-01 10:30:00"

    def test_add_days(self):
        """Test add_days method."""
        dt = datetime(2023, 12, 1)
        new_dt = DateHelper.add_days(dt, 5)
        assert new_dt.day == 6


class TestHTTPClient:
    """Test HTTPClient class."""

    def test_init(self):
        """Test initialization."""
        client = HTTPClient("https://api.example.com")
        assert client.base_url == "https://api.example.com"
        assert client.timeout == 30

    def test_init_with_trailing_slash(self):
        """Test initialization with trailing slash."""
        client = HTTPClient("https://api.example.com/")
        assert client.base_url == "https://api.example.com"