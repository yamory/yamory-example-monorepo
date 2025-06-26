# npm workspace サンプルアプリケーション

このプロジェクトは npm workspaces を使用したサンプルアプリケーションです。

## プロジェクト構成

```
npm-util-2/
├── packages/
│   ├── shared/          # 共通ライブラリ（型定義、ユーティリティ）
│   ├── frontend/        # React フロントエンドアプリ
│   └── mock-api/        # Express モックAPIサーバー
└── package.json         # ワークスペース設定
```

## セットアップ

1. 依存関係のインストール:
```bash
npm install
```

2. 共通ライブラリのビルド:
```bash
npm run build --workspace=shared
```

3. モックAPIサーバーのビルド:
```bash
npm run build --workspace=mock-api
```

## 実行方法

### 開発環境で同時実行
```bash
npm run dev:all
```

### 個別実行
```bash
# モックAPIサーバーのみ
npm run start:mock

# フロントエンドのみ
npm run dev
```

## 各パッケージの詳細

### @workspace/shared
- 型定義 (`User`, `Todo`, `ApiResponse`)
- ユーティリティ関数
- TypeScriptで書かれた共通ライブラリ

### @workspace/frontend
- React + TypeScript
- ポート3000で実行
- 共通ライブラリを使用

### @workspace/mock-api
- Express + TypeScript
- ポート3001で実行
- Todo管理のRESTful API
- 共通ライブラリの型を使用