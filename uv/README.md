# Python Monorepo Utility

uvを使用したマルチプロジェクト構成のPythonアプリケーション

## プロジェクト構成

このモノレポには以下のパッケージが含まれています：

- **core** - 共通のライブラリとユーティリティ
- **web-api** - FastAPIを使用したWebAPIサーバー
- **cli** - コマンドラインツール
- **worker** - Celeryを使用したバックグラウンドワーカー

## セットアップ

### 前提条件

- Python 3.11以上
- [uv](https://github.com/astral-sh/uv) パッケージマネージャー

### インストール

```bash
# プロジェクトのクローン
git clone <repository-url>
cd python-monorepo-util-2

# 依存関係のインストール
uv sync

# 開発環境の有効化
source .venv/bin/activate  # Linux/macOS
# または
.venv\Scripts\activate     # Windows
```

## 使用方法

### Web API の起動

```bash
uv run --package web-api uvicorn main:app --reload
```

### CLI ツールの実行

```bash
uv run --package cli python -m cli_app --help
```

### Worker の起動

```bash
uv run --package worker celery -A worker_app worker --loglevel=info
```

## 開発

### テストの実行

```bash
# 全てのテストを実行
uv run pytest

# 特定のパッケージのテストのみ実行
uv run pytest packages/core/tests/
```

### コードフォーマット

```bash
# コードのフォーマット
uv run black .
uv run ruff check --fix .

# 型チェック
uv run mypy .
```

## パッケージの追加

新しいパッケージを追加する場合：

1. `packages/` ディレクトリに新しいパッケージを作成
2. `pyproject.toml` の `tool.uv.workspace.members` にパッケージを追加
3. パッケージの `pyproject.toml` を設定