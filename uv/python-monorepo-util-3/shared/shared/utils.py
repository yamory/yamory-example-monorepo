"""Common utility functions."""

import requests
from datetime import datetime, timedelta
from typing import Dict, Any, Optional
from dateutil import parser


class DateHelper:
    """Date utility helper."""

    @staticmethod
    def now() -> datetime:
        """Get current datetime."""
        return datetime.now()

    @staticmethod
    def parse_date(date_str: str) -> datetime:
        """Parse date string to datetime object."""
        return parser.parse(date_str)

    @staticmethod
    def format_date(dt: datetime, format_str: str = "%Y-%m-%d %H:%M:%S") -> str:
        """Format datetime to string."""
        return dt.strftime(format_str)

    @staticmethod
    def add_days(dt: datetime, days: int) -> datetime:
        """Add days to datetime."""
        return dt + timedelta(days=days)


class HTTPClient:
    """HTTP client wrapper."""

    def __init__(self, base_url: str = "", timeout: int = 30):
        self.base_url = base_url.rstrip('/')
        self.timeout = timeout
        self.session = requests.Session()

    def get(self, endpoint: str, params: Optional[Dict[str, Any]] = None) -> requests.Response:
        """Make GET request."""
        url = f"{self.base_url}{endpoint}" if self.base_url else endpoint
        return self.session.get(url, params=params, timeout=self.timeout)

    def post(self, endpoint: str, json_data: Optional[Dict[str, Any]] = None) -> requests.Response:
        """Make POST request."""
        url = f"{self.base_url}{endpoint}" if self.base_url else endpoint
        return self.session.post(url, json=json_data, timeout=self.timeout)

    def put(self, endpoint: str, json_data: Optional[Dict[str, Any]] = None) -> requests.Response:
        """Make PUT request."""
        url = f"{self.base_url}{endpoint}" if self.base_url else endpoint
        return self.session.put(url, json=json_data, timeout=self.timeout)

    def delete(self, endpoint: str) -> requests.Response:
        """Make DELETE request."""
        url = f"{self.base_url}{endpoint}" if self.base_url else endpoint
        return self.session.delete(url, timeout=self.timeout)