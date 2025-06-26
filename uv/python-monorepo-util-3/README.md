# Python Multi-Project Application

This is a multi-project Python application structure using pipenv for dependency management.

## Project Structure

```
python-monorepo-util-3/
├── shared/          # Common shared library
├── api/             # Web API application
├── cli/             # Command line interface tool
└── README.md        # This file
```

## Setup

Each project has its own Pipenv environment:

### Shared Library
```**bash**
cd shared
pipenv install
pipenv shell
```

### API Application
```bash
cd api
pipenv install
pipenv shell
```

### CLI Tool
```bash
cd cli
pipenv install
pipenv shell
```

## Development

Each project can be developed independently while sharing common functionality through the shared library.

### Running the API
```bash
cd api
pipenv run python main.py
```

### Running the CLI
```bash
cd cli
pipenv run python main.py --help
```

## Dependencies

- Each project maintains its own `Pipfile` and `Pipfile.lock`
- Shared library can be installed as a local dependency in other projects
- Development dependencies are isolated per project