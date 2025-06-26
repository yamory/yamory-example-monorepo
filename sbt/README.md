# SBT Monorepo Util

sbtのマルチプロジェクト構成で作成したシンプルなScalaプロジェクトです。

## プロジェクト構成

```
sbt-monorepo-util/
├── build.sbt                 # ルートビルド設定
├── project/
│   ├── build.properties      # sbtバージョン
│   └── plugins.sbt           # sbtプラグイン
├── core/                     # コアライブラリ
│   └── src/
│       ├── main/scala/
│       │   └── com/example/core/
│       │       ├── User.scala        # Userケースクラス
│       │       └── UserService.scala # UserServiceクラス
│       └── test/scala/
│           └── com/example/core/
│               ├── UserSpec.scala        # Userテスト
│               └── UserServiceSpec.scala # UserServiceテスト
├── api/                      # APIレイヤー
│   └── src/main/scala/
│       └── com/example/api/
│           └── UserRoutes.scala # REST APIルーティング
└── main/                     # メインアプリケーション
    └── src/main/scala/
        └── com/example/main/
            └── Main.scala    # メインクラス
```

## 使用技術

- Scala 2.13
- SBT 1.9.6
- Cats（関数型プログラミング）
- Akka HTTP（REST API）
- ScalaTest（テスト）

## 機能

### Core モジュール
- `User`: ユーザー情報を管理するケースクラス
- `UserService`: ユーザーのCRUD操作を提供するサービス

### API モジュール
- `UserRoutes`: REST APIエンドポイントの定義
- JSON形式でのデータ交換

### Main モジュール
- HTTPサーバーの起動
- サンプルデータの投入

## 使い方

### 1. プロジェクトのビルド
```bash
sbt compile
```

### 2. テストの実行
```bash
# 全てのテストを実行
sbt test

# 特定のプロジェクトのテストのみ実行
sbt core/test
```

### 3. アプリケーションの起動
```bash
sbt main/run
```

サーバーは `http://localhost:8080` で起動します。

### 4. APIエンドポイント

#### ヘルスチェック
```bash
curl http://localhost:8080/health
```

#### ユーザー一覧取得
```bash
curl http://localhost:8080/api/users
```

#### ユーザー取得
```bash
curl http://localhost:8080/api/users/1
```

#### ユーザー作成
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "David", "email": "david@example.com"}'
```

#### ユーザー更新
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Updated", "email": "alice.updated@example.com"}'
```

#### ユーザー削除
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## 開発用コマンド

### sbt shell
```bash
sbt
```

sbt shellから以下のコマンドが利用できます：
- `compile`: 全プロジェクトのコンパイル
- `core/compile`: coreプロジェクトのみコンパイル
- `test`: 全テストの実行
- `main/run`: メインアプリケーションの実行
- `main/assembly`: 実行可能JARの作成

### 実行可能JARの作成
```bash
sbt main/assembly
```

生成されたJARファイルは以下で実行できます：
```bash
java -jar main/target/scala-2.13/app.jar
```

## プロジェクトの拡張

このプロジェクトは基本的な構成を提供しています。以下のような拡張が可能です：

- データベース接続（Slick、Doobie）
- 認証・認可機能
- ログ機能（Logback）
- 設定管理（Typesafe Config）
- Docker対応
- CI/CD設定