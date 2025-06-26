#!/bin/bash

echo "🚀 Yarn Workspace App セットアップ開始"
echo "=================================="

# Yarnのバージョンチェック
if ! command -v yarn &>/dev/null; then
    echo "❌ Yarnがインストールされていません"
    echo "   以下のコマンドでインストールしてください:"
    echo "   npm install -g yarn"
    exit 1
fi

echo "✅ Yarn バージョン: $(yarn --version)"

# 依存関係のインストール
echo ""
echo "📦 依存関係をインストール中..."
yarn install

# 共通ライブラリのビルド
echo ""
echo "🔧 共通ライブラリをビルド中..."
yarn workspace @workspace/shared build

echo ""
echo "✅ セットアップ完了！"
echo ""
echo "🎉 使用方法:"
echo "   開発サーバー起動: yarn dev"
echo "   フロントエンドのみ: yarn workspace frontend dev"
echo "   バックエンドのみ: yarn workspace backend dev"
echo ""
echo "🌐 アクセス先:"
echo "   フロントエンド: http://localhost:3000"
echo "   バックエンド API: http://localhost:3001"
echo ""
