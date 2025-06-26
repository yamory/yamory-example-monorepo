# Gradle マルチプロジェクト構成 - 計算機アプリケーション

このプロジェクトは、Gradleのマルチプロジェクト構成を使用した簡単な計算機アプリケーションです。

## プロジェクト構成

```
gradle-monorepo-util/
├── core/              # 共通ライブラリ（計算機能）
├── app/               # コマンドラインアプリケーション
├── web/               # Webアプリケーション
├── build.gradle       # ルートプロジェクト設定
└── settings.gradle    # プロジェクト構成定義
```

## モジュール詳細

### core
- 基本的な計算機能（加算、減算、乗算、除算、べき乗）を提供
- ログ機能付き
- JUnit 5を使用したテスト

### app
- coreモジュールを使用したコマンドライン計算機
- 対話的な操作が可能

### web
- coreモジュールを使用したWeb計算機
- JettyサーバーでHTMLとAPIを提供
- シンプルなWebインターフェース

## 必要な環境

- Java 17以降
- Gradle（Wrapperを使用するため不要）

### Java 17の環境設定

SDKMANを使用している場合：
```bash
sdk use java 17.0.8-zulu
```

## Java 17の新機能を活用

このプロジェクトでは以下のJava 17の新機能を活用しています：

- **Switch式 (Java 14+)**: より簡潔で読みやすい条件分岐
- **テキストブロック (Java 15+)**: HTMLなどの複数行文字列を見やすく記述
- **レコードクラス (Java 14+)**: データ保持のためのイミュータブルクラス
- **var型推論 (Java 10+)**: ローカル変数の型推論でコードを簡潔に

## ビルドと実行

### 全体のビルド
```bash
./gradlew build
```

### テスト実行
```bash
./gradlew test
```

### コマンドラインアプリの実行
```bash
./gradlew :app:run
```

### Webアプリケーションの実行
```bash
./gradlew :web:run
```

Webアプリケーションは http://localhost:8080 でアクセスできます。

### 各モジュール個別のビルド
```bash
# coreモジュールのみ
./gradlew :core:build

# appモジュールのみ
./gradlew :app:build

# webモジュールのみ
./gradlew :web:build
```

## 使用技術

- **Java 17**: プログラミング言語
- **Gradle**: ビルドツール
- **SLF4J + Logback**: ログ機能
- **JUnit 5**: テストフレームワーク
- **Jetty**: Webサーバー（webモジュール）
- **Gson**: JSON処理（webモジュール）

## ディレクトリ構造の詳細

```
├── core/
│   ├── build.gradle
│   └── src/
│       ├── main/java/com/example/core/
│       │   └── Calculator.java
│       └── test/java/com/example/core/
│           └── CalculatorTest.java
├── app/
│   ├── build.gradle
│   └── src/main/java/com/example/app/
│       └── Application.java
└── web/
    ├── build.gradle
    └── src/main/java/com/example/web/
        └── WebApplication.java
```