# pnpmマルチワークスペース サンプルアプリ

このプロジェクトは、pnpmのマルチワークスペース機能を使用したシンプルなタスク管理アプリケーションです。

## 構成

- **packages/shared**: 共通ライブラリ（型定義とユーティリティ関数）
- **packages/backend**: Express.jsを使用したRESTful API
- **packages/frontend**: ReactとViteを使用したWebフロントエンド

## 前提条件

- Node.js 18以上
- pnpm 8以上

## インストールと起動

### 1. 依存関係のインストール

```bash
pnpm install
```

### 2. 共通ライブラリのビルド

```bash
pnpm --filter shared build
```

### 3. 開発サーバーの起動

全体を同時に起動（推奨）:
```bash
pnpm dev
```

または個別に起動:

バックエンドのみ:
```bash
pnpm --filter backend dev
```

フロントエンドのみ:
```bash
pnpm --filter frontend dev
```

### 4. アクセス

- フロントエンド: http://localhost:3000
- バックエンドAPI: http://localhost:3001

## API エンドポイント

### ユーザー関連
- `GET /api/users` - 全ユーザー取得
- `GET /api/users/:id` - 特定ユーザー取得
- `POST /api/users` - ユーザー作成

### タスク関連
- `GET /api/tasks` - 全タスク取得
- `GET /api/tasks/:id` - 特定タスク取得
- `POST /api/tasks` - タスク作成
- `PUT /api/tasks/:id` - タスク更新
- `DELETE /api/tasks/:id` - タスク削除

## 機能

- タスクの作成、表示、完了/未完了切り替え、削除
- ユーザー管理
- レスポンシブデザイン
- 共通ライブラリによる型安全性

## ワークスペースの特徴

このプロジェクトでは以下のpnpm機能を活用しています:

1. **ワークスペース依存関係**: `workspace:*` プロトコルを使用して内部パッケージを参照
2. **フィルタリング**: `--filter` オプションを使用して特定のパッケージのみ実行
3. **並列実行**: `concurrently` を使用してフロントエンドとバックエンドを同時起動
4. **共通ライブラリ**: 型定義やユーティリティ関数を共有

## ビルド

全体をビルド:
```bash
pnpm build
```

個別にビルド:
```bash
pnpm --filter shared build
pnpm --filter backend build
pnpm --filter frontend build
```

## クリーンアップ

```bash
pnpm clean
```