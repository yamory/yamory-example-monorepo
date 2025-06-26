from setuptools import setup, find_packages

setup(
    name="shared",
    version="0.1.0",
    packages=find_packages(),
    install_requires=[
        "requests",
        "pydantic",
        "python-dateutil",
    ],
    author="Developer",
    author_email="dev@example.com",
    description="Shared utility library for multi-project application",
    python_requires=">=3.8",
)