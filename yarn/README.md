# Yarn Workspace App

Yarnワークスペースを使用したマルチパッケージアプリケーションです。

## 構成

- `packages/frontend` - React フロントエンドアプリケーション
- `packages/backend` - Express.js バックエンドAPI
- `packages/shared` - 共通ライブラリ（型定義など）

## セットアップ

```bash
# 依存関係のインストール
yarn install

# 開発サーバーの起動（フロントエンド + バックエンド）
yarn dev

# 全パッケージのビルド
yarn build

# テスト実行
yarn test

# リンティング
yarn lint
```

## 個別パッケージのコマンド

```bash
# フロントエンドのみ起動
yarn workspace frontend dev

# バックエンドのみ起動
yarn workspace backend dev

# 特定のパッケージにパッケージを追加
yarn workspace frontend add react-router-dom
yarn workspace backend add cors
```

## ポート設定

- フロントエンド: http://localhost:3000
- バックエンド: http://localhost:3001